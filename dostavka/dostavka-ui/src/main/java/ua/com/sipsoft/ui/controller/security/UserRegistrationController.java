package ua.com.sipsoft.ui.controller.security;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.service.dto.user.UserDto;
import ua.com.sipsoft.service.dto.user.UserRegistrationDto;
import ua.com.sipsoft.service.exception.UserDtoAuditExeption;
import ua.com.sipsoft.service.user.UserService;
import ua.com.sipsoft.ui.model.request.mapper.ToUserRegDtoMapper;
import ua.com.sipsoft.ui.model.request.user.UserRegReq;
import ua.com.sipsoft.util.AppURL;
import ua.com.sipsoft.util.message.LoginMsg;
import ua.com.sipsoft.util.message.UserEntityCheckMsg;
import ua.com.sipsoft.util.security.Role;

@Controller
@RequestMapping(AppURL.LOGIN_REGISTRATION)
@Slf4j
@RequiredArgsConstructor
public class UserRegistrationController {

	private final UserService userService;

	@ModelAttribute("user")
	public UserRegReq userRegistrationRequest() {
		return new UserRegReq();
	}

	@GetMapping
	public String showRegistrationForm(@ModelAttribute("user") UserRegReq userRegistrationRequest,
			BindingResult result, Model model) {
		log.info("IN showRegistrationForm - Request registrationn form");
		return AppURL.LOGIN_REGISTRATION;
	}

	@PostMapping
	public String registerUserAccount(@ModelAttribute("user") UserRegReq userRegistrationRequest,
			BindingResult result, Model model, @RequestParam(value = "action") Optional<String> action) {

		log.info("IN registerUserAccount - Request register new user '{}'", userRegistrationRequest);

		if (!StringUtils.equals(userRegistrationRequest.getPassword(), userRegistrationRequest.getConfirmPassword())) {
			log.info("IN registerUserAccount - Registration rejected. Password and Confirm Paasword does not match.");
			result.rejectValue("confirmPassword", UserEntityCheckMsg.PASS_EQUAL,
					"the entered passwords does not match.");
		}

		if (!userRegistrationRequest.getTerms()) {
			log.info("IN registerUserAccount - Registration rejected. Terms are not adreed.");
			result.rejectValue("terms", LoginMsg.SIGNUP_REJECT_TERMS,
					"You must agree to the terms of service before continuing.");
		}

		if (result.hasErrors()) {
			log.info("IN registerUserAccount - Registration rejected. Return to form.");
			return AppURL.LOGIN_REGISTRATION;
		}

		UserRegistrationDto userRegistrationDto = ToUserRegDtoMapper.MAPPER
				.fromUserRegReq(userRegistrationRequest);

		if (userRegistrationDto.getRoles().isEmpty()) {
			userRegistrationDto.addRoles(Role.ROLE_USER);
		}

		try {
			log.info("IN registerUserAccount - Perform register User");

			Optional<UserDto> userDtoO = userService.registerNewUser(userRegistrationDto);
			if (userDtoO.isPresent()) {
				log.info("IN registerUserAccount - Registration is successful. Inform to the registrant");
				return "redirect:" + AppURL.LOGIN_REGISTRATION + "?success";
			}
		} catch (UserDtoAuditExeption ex) {
			ex.getAuditMessages().forEach((k, v) -> result.addError(new FieldError("user", k, v)));
			log.info("IN registerUserAccount - Registration rejected. Return to form.");
			return AppURL.LOGIN_REGISTRATION;
		}

		log.info("IN registerUserAccount - Registration failed. Inform registrant.");
		result.rejectValue("", LoginMsg.SIGNUP_FAILED, "Failed to register user.");
		return AppURL.LOGIN_REGISTRATION;
	}

}