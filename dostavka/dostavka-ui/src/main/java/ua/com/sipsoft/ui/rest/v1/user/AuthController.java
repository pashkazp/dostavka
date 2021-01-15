package ua.com.sipsoft.ui.rest.v1.user;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.service.dto.user.UserDto;
import ua.com.sipsoft.service.dto.user.UserRegistrationDto;
import ua.com.sipsoft.service.security.jwt.JwtTokenProvider;
import ua.com.sipsoft.service.user.UserService;
import ua.com.sipsoft.ui.model.request.LoginRequest;
import ua.com.sipsoft.ui.model.request.SignUpRequest;
import ua.com.sipsoft.ui.model.response.InfoResponse;
import ua.com.sipsoft.ui.model.response.AbstractSubInfoResponse;
import ua.com.sipsoft.ui.model.response.ValidationInfoResponse;
import ua.com.sipsoft.util.AppProperties;
import ua.com.sipsoft.util.AppURL;
import ua.com.sipsoft.util.I18NProvider;
import ua.com.sipsoft.util.audit.AuditResponse;
import ua.com.sipsoft.util.audit.CreateRequestPropertyAuditor;
import ua.com.sipsoft.util.message.AppNotifyMsg;
import ua.com.sipsoft.util.message.RestV1Msg;
import ua.com.sipsoft.util.security.Role;

/**
 * The Class AuthController used for handles requests of login and signUp of
 * Users.
 * 
 * @author Pavlo Degtyaryev
 * 
 */
@RestController
@RequestMapping(AppURL.API_V1_AUTH)
@RequiredArgsConstructor
@Slf4j
public class AuthController {

	/** The authentication manager. */
	private final AuthenticationManager authenticationManager;

	/** The users service. */
	private final UserService userService;

	/** The jwt token provider. */
	private final JwtTokenProvider jwtTokenProvider;

	/** The validator of User registration. */
	private final CreateRequestPropertyAuditor<UserRegistrationDto> userRegistrationDtoAuditor;

	private final I18NProvider i18n;

	private final AppProperties appProperties;

	/**
	 * Authenticate user.
	 *
	 * @param loginRequest the login request
	 * @return the response entity
	 */
	@PostMapping(AppURL.LOGIN)
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		log.debug("Perform authenticate user with loginRequest: {}", loginRequest);
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getEmail(),
						loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtTokenProvider.createToken(authentication);

		log.debug("Create Token base on authentication and return it.");
		return ResponseEntity.status(HttpStatus.OK).body(appProperties.getAuth().getTokenPrefix() + token);
	}

	/**
	 * Register new User.
	 *
	 * @param SignUpRequest the sign up request
	 * @return the response entity
	 * @throws URISyntaxException
	 */
	@PostMapping(AppURL.SIGNUP)
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest)
			throws URISyntaxException {

		log.info("IN registerUser - Perform register User with signUp request: {}", signUpRequest);
		UserRegistrationDto userRegDto = new UserRegistrationDto();
		Locale loc = LocaleContextHolder.getLocale();

		userRegDto.setEmail(signUpRequest.getEmail());
		userRegDto.setName(signUpRequest.getName());
		userRegDto.setPassword(signUpRequest.getPassword());
		userRegDto.addRoles(Role.ROLE_USER);

		AuditResponse response = userRegistrationDtoAuditor.inspectNewData(userRegDto, null, loc);
		response.setValid(response.getMessages().isEmpty());
		if (response.isInvalid()) {
			log.info("IN registerUser - Request validation is fail. Inform to the registrant");

			InfoResponse infoResponse = new InfoResponse(HttpStatus.BAD_REQUEST,
					i18n.getTranslation(RestV1Msg.USER_NEW_CHECK_FAIL, loc),
					i18n.getTranslation(RestV1Msg.USER_NEW_CHECK_FAIL_EXT, loc));

			if (!response.isEmpty()) {
				infoResponse.setSubInfos(new ArrayList<AbstractSubInfoResponse>());
				response.getMessages()
						.forEach((k, v) -> infoResponse.getSubInfos().add(new ValidationInfoResponse(k, v)));
			}
			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());
		}

		log.info("IN registerUser - Perform store created User");

		Optional<UserDto> userDtoO = userService.registerNewUser(userRegDto, false);
		if (userDtoO.isPresent()) {
			log.info("IN registerUser - Registration is successful. Inform to the registrant");
			URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path(AppURL.API_V1_USERS).path("/{id}")
					.buildAndExpand(userDtoO.get().getId()).toUri();
			return ResponseEntity.created(location)
					.body(response);
		}

		log.info("IN registerUser - Registration failed. Inform registrant.");
		response.setValid(false);
		response.addMessage("", i18n.getTranslation(AppNotifyMsg.USER_NOT_ADDED, loc));

		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

}
