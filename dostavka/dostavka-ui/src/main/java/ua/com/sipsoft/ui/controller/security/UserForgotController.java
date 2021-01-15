package ua.com.sipsoft.ui.controller.security;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.dto.VerificationTokenDto;
import ua.com.sipsoft.service.security.OnRememberPasswordEvent;
import ua.com.sipsoft.service.security.VerificationTokenService;
import ua.com.sipsoft.service.user.UserService;
import ua.com.sipsoft.ui.model.response.MessageDto;
import ua.com.sipsoft.ui.model.response.TwoMessageDto;
import ua.com.sipsoft.util.AppURL;
import ua.com.sipsoft.util.message.LoginMsg;
import ua.com.sipsoft.util.message.UserEntityCheckMsg;
import ua.com.sipsoft.util.security.AgreedEmailCheck;
import ua.com.sipsoft.util.security.AgreedPasswordCheck;
import ua.com.sipsoft.util.security.VerificationTokenType;

@Controller
@RequestMapping(AppURL.LOGIN_FORGOT)
@Slf4j
@RequiredArgsConstructor
public class UserForgotController {

	private final UserService userService;
	private final VerificationTokenService tokenService;
	private final PasswordEncoder passwordEncoder;
	private final ApplicationEventPublisher eventPublisher;

	@ModelAttribute("useremail")
	public MessageDto userRegistrationDto() {
		return new MessageDto();
	}

	@ModelAttribute("requestpassword")
	public TwoMessageDto requestPasswordDto() {
		return new TwoMessageDto();
	}

	@GetMapping()
	public String showForgotForm(@ModelAttribute("useremail") MessageDto messageDto, Model model) {
		log.info("Get form for request password changes.");
		return "/login/forgot";
	}

	@PostMapping()
	public String sendForgotEmail(@ModelAttribute("useremail") MessageDto messageDto,
			Model model, BindingResult result) {

		log.info("Requesting a password change for User by Email '{}'", messageDto.getMessage());
		if (messageDto == null || StringUtils.isBlank(messageDto.getMessage())) {
			log.info("Requested Email '{}' for the password change request is short",
					messageDto.getMessage());
			result.rejectValue("message", LoginMsg.EMAIL_SHORT);
		}

		if (result.hasErrors()) {
			log.info("Reques has error end return to caller");
			return "/login/forgot";
		}

		User user = null;
		String remember = messageDto.getMessage();
		if (AgreedEmailCheck.agreedEmailCheck(remember)) {
			log.info("Try to find User by Email '{}'", remember);
			user = userService.fetchByEmail(remember).orElse(null);
		}
//		else {
//			log.info("Try to find User by Username '{}'", remember);
//			user = userService.fetchByUsername(remember).orElse(null);
//		}

		if (user != null) {
			log.info("User found. Regiser sendmail event.");
			eventPublisher.publishEvent(
					new OnRememberPasswordEvent(user, LocaleContextHolder.getLocale()));
		}

		log.info("Paasword change request was maked.");
		return "redirect:/login/forgot?success";

	}

	@GetMapping(AppURL.REQUEST)
	public String showRequestNewPasswordForm(@ModelAttribute("requestpassword") TwoMessageDto twoMessageDto,
			BindingResult result,
			@RequestParam(value = "token") Optional<String> token,
			@RequestParam(value = "success") Optional<String> success, Model model) {

		log.info("Request to change password by token '{}', param 'success' is '{}',", token.orElse(null),
				success.isPresent());

		if (success.isPresent()) {
			log.info("Show message about successfully password changes");
			return "/login/forgotrequest";
		}

		if (token.isEmpty() || StringUtils.isBlank(token.get())) {
			log.warn("Forgot Password verification token is missed");
			result.reject(LoginMsg.TOKEN_LACKED, "Security token is lacking. Are you sure about your actions?");
			return "/login/forgotrequest";
		}
		model.addAttribute("token", token.get());
		log.info("Put token '{}' to html form and request new password", token.get());
		return "/login/forgotrequest";
	}

	@PostMapping(AppURL.REQUEST)
	public String sendCahangePasswordRequest(@RequestParam(value = "token") Optional<String> token,
			@ModelAttribute("requestpassword") TwoMessageDto twoMessageDto,
			BindingResult result, Model model) {

		log.info("Resieve password for perform password changes by token '{}'.", token);

		if (token.isEmpty() || StringUtils.isBlank(token.get())) {
			log.info("Security token is lacking. Request rejected.");
			result.reject(LoginMsg.TOKEN_LACKED, "Security token is lacking. Are you sure about your actions?");
			return "/login/forgotrequest";
		}

		model.addAttribute("token", token.get());
		VerificationTokenDto vTokenDto = tokenService.fetchDtoByToken(token.get()).orElse(null);

		if (vTokenDto == null || VerificationTokenType.FORGOTPASS != vTokenDto.getTokenType()) {
			log.info("The token you entered was not found. Request rejected.");
			result.reject(LoginMsg.TOKEN_MISSED,
					"The token you entered was not found. Make sure the data you entered is correct.");
			return "/login/forgotrequest";
		}

		if (LocalDateTime.now().isAfter(vTokenDto.getExpiryDate())) {
			log.info("The token used has a limited life span and has expired. Request rejected.");
			result.reject(LoginMsg.TOKEN_EXPIRED, "The token used has a limited life span and has expired.");
			return "/login/forgotrequest";
		}

		if (vTokenDto.getUsed()) {
			log.info("This security token has already been used. Request rejected.");
			result.reject(LoginMsg.TOKEN_USED, "This security token has already been used");
			return "/login/forgotrequest";
		}

		if (twoMessageDto == null || !AgreedPasswordCheck.adreedPasswordCheck(twoMessageDto.getMessageOne())) {
			log.info("The new password has not allowed characters. Request rejected.");
			result.rejectValue("messageOne", UserEntityCheckMsg.PASS_CHR);
		}

		if (twoMessageDto != null
				&& StringUtils.compare(twoMessageDto.getMessageOne(), twoMessageDto.getMessageTwo()) != 0) {
			log.info("Entered new passwords are not same. Request rejected.");
			result.rejectValue("messageTwo", UserEntityCheckMsg.PASS_EQUAL, "");
		}

		if (result.hasErrors()) {
			log.info("Request has errors. Request rejected.");
			return "/login/forgotrequest";
		}

		User user = vTokenDto.getUser();

		log.info("Encode new password for User #'{}' '{}' and save it.", user.getId(), user.getName());
		user.setPassword(passwordEncoder.encode(twoMessageDto.getMessageOne()));
		user = userService.saveUser(user);

		if (user != null) {
			log.info("Set token '{}' is used and end operation.", token.get());
			tokenService.setTokenUsed(token.get());
			return "redirect:/login/forgot/request?success";
		}
		result.reject(LoginMsg.PASSWORD_RESET_WRONG, "Password reset failed.");
		return "/login/forgotrequest";
	}
}