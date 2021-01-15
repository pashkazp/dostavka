package ua.com.sipsoft.ui.rest.v1.user;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.collect.Multimap;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.service.dto.user.UserDto;
import ua.com.sipsoft.service.dto.user.UserRegistrationDto;
import ua.com.sipsoft.service.dto.user.UserUpdateDto;
import ua.com.sipsoft.service.exception.UserDtoAuditExeption;
import ua.com.sipsoft.service.security.UserPrincipal;
import ua.com.sipsoft.service.user.UserFilter;
import ua.com.sipsoft.service.user.UserService;
import ua.com.sipsoft.ui.model.request.mapper.ToUserRegistrationDtoMapper;
import ua.com.sipsoft.ui.model.request.mapper.ToUserUpdateDtoMapper;
import ua.com.sipsoft.ui.model.request.user.UserRegistrationRequest;
import ua.com.sipsoft.ui.model.request.user.UserUpdateRequest;
import ua.com.sipsoft.ui.model.response.AbstractSubInfoResponse;
import ua.com.sipsoft.ui.model.response.InfoResponse;
import ua.com.sipsoft.ui.model.response.ValidationInfoResponse;
import ua.com.sipsoft.ui.model.response.mapper.UserRestMapper;
import ua.com.sipsoft.ui.model.response.user.UserRest;
import ua.com.sipsoft.ui.util.audit.UserRegistrationRequestConfirmPasswordAuditor;
import ua.com.sipsoft.ui.util.audit.UserUpdateRequestConfirmPasswordAuditor;
import ua.com.sipsoft.util.AppURL;
import ua.com.sipsoft.util.I18NProvider;
import ua.com.sipsoft.util.audit.AuditResponse;
import ua.com.sipsoft.util.message.RestV1Msg;
import ua.com.sipsoft.util.paging.Page;
import ua.com.sipsoft.util.paging.PagingRequest;
import ua.com.sipsoft.util.security.CurrentUser;
import ua.com.sipsoft.util.security.Role;

/**
 * Class UsersRestController handles rest requests for Creape Update and Get
 * Users.
 *
 * @author Pavlo Degtyaryev
 * @version 1.0
 */

@Slf4j
@RestController
@RequestMapping(AppURL.API_V1_USERS)
@RequiredArgsConstructor
public class UsersRestController {

	/** The users service. */
	private final UserService userService;

	/** The i18n provider. */
	private final I18NProvider i18n;

	/** The confirm registred password auditor. */
	private final UserRegistrationRequestConfirmPasswordAuditor cPasswordAuditor;

	/** The confirm updated password auditor. */
	private final UserUpdateRequestConfirmPasswordAuditor cUPasswordAuditor;

	/**
	 * Return List of all {@link UserRest}.
	 *
	 * @return the {@link List} of {@link UserRest}
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> listAllUsers() {
		log.debug("IN listAllUserRest - Get the list of all Users");
		// TODO return only Users approved for Editor
		List<UserDto> users = userService.getAllUsersDto();
		List<UserRest> userRests = UserRestMapper.MAPPER.toRest(users);
		if (userRests.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(userRests);
	}

	/**
	 * Gets the Users Page.
	 *
	 * @param pagingRequest the {@link PagingRequest}
	 * @param loc           the loc
	 * @param principal     the principal
	 * @return the users {@link Page}
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = AppURL.PAGES, consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getUsersPage(@RequestBody(required = true) PagingRequest pagingRequest, Locale loc,
			Principal principal) {
		log.debug("IN getUsersPage - Get page of Users '{}'", pagingRequest.toString());

		UserFilter userFilter = new UserFilter();
		if (StringUtils.isNotBlank(pagingRequest.getSearch().getValue())) {
			userFilter.setName(pagingRequest.getSearch().getValue());
		}
		// TODO return only Users approved for Editor
		Page<UserDto> page = userService.getFilteredPage(pagingRequest, userFilter);

		List<UserDto> data = page.getData();
		List<UserRest> usersList = UserRestMapper.MAPPER.toRest(data);
		Page<UserRest> pageRest = new Page<>(page, usersList);
		return new ResponseEntity<>(pageRest, HttpStatus.PARTIAL_CONTENT);
	}

	/**
	 * Gets the {@link UserRest} by User id.
	 *
	 * @param userId the user id
	 * @return the {@link UserRest}
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/{userId}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<UserRest> getUserById(@PathVariable(value = "userId") Long userId) {

		log.debug("IN getUserById - Get User by id {}", userId);
		// TODO return only Users approved for Editor
		Optional<UserDto> userDtoO = userService.fetchDtoById(userId);
		if (userDtoO.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		UserRest userRest = UserRestMapper.MAPPER.toRest(userDtoO.get());
		return ResponseEntity.ok(userRest);
	}

	/**
	 * Adds the new User by registration request.
	 *
	 * @param userCreationRequest the user creation request
	 * @param loc                 the Locale
	 * @param principal           the user principal
	 * @return the response entity
	 * @throws URISyntaxException the URI syntax exception
	 */
	@PostMapping(value = "", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> addNewUserByCreationRequest(
			@RequestBody(required = false) UserRegistrationRequest userCreationRequest, Locale loc, Principal principal)
			throws URISyntaxException {

		log.info("IN addNewUserByCreationRequest - Request register new user '{}'", userCreationRequest);

		if (userCreationRequest == null) {
			log.warn("IN addNewUserByCreationRequest - User creation request must be not null");
			InfoResponse infoResponse = new InfoResponse(HttpStatus.BAD_REQUEST,
					i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST, loc),
					i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST_EXT, loc));

			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());
		}

		AuditResponse response = cPasswordAuditor.inspectNewData(userCreationRequest, null, loc);

		if (response.isInvalid()) { // if there are any errors
			log.info("IN addNewUserByCreationRequest - Request validation is fail. Inform to the registrant");

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

		UserRegistrationDto userRegDto = ToUserRegistrationDtoMapper.MAPPER
				.fromUserRegistrationRequest(userCreationRequest);

		if (userRegDto.getRoles().isEmpty()) {
			userRegDto.addRoles(Role.ROLE_USER);
		}

		log.info("IN addNewUserByCreationRequest - Perform register User");
		Optional<UserDto> userDtoO = userService.registerNewUser(userRegDto, false);

		if (userDtoO.isEmpty()) { // Registration is fail
			log.info("IN addNewUserByCreationRequest - Registration is fail. Inform to the registrant");
			InfoResponse infoResponse = new InfoResponse(HttpStatus.INTERNAL_SERVER_ERROR,
					i18n.getTranslation(RestV1Msg.USER_NEW_FAIL, loc),
					i18n.getTranslation(RestV1Msg.USER_NEW_FAIL_EXT, loc));

			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());
		}

		log.info("IN addNewUserByCreationRequest - Registration is successful. Inform to the registrant");
		UserRest userRest = UserRestMapper.MAPPER.toRest(userDtoO.get());
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path(AppURL.API_V1_USERS).path("/{id}")
				.buildAndExpand(userRest.getId()).toUri();
		return ResponseEntity.created(location).body(userRest);

	}

	/**
	 * Update existing User by User id.
	 *
	 * @param userId            the user id
	 * @param userUpdateRequest the FacilityRegistrationDto
	 * @param loc               the Locale
	 * @param principal         the Principal
	 * @return the user data check response
	 */
	@Operation(summary = "Update User", description = "Update existing User information", responses = {
			@ApiResponse(responseCode = "202", description = "User updated"),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "404", description = "User not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@PutMapping(value = "/{userId}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> updateUser(
			@PathVariable(value = "userId") Long userId,
			@RequestBody(required = false) UserUpdateRequest userUpdateRequest,
			Locale loc, Principal principal) {

		log.info("IN updateUser - Request register update id='{}' by user data '{}'", userId, userUpdateRequest);

		if (userUpdateRequest == null) {
			log.warn("IN updateUser - User update request must be not null");
			InfoResponse infoResponse = new InfoResponse(HttpStatus.BAD_REQUEST,
					i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST, loc),
					i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST_EXT, loc));

			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());
		}
		AuditResponse response = cUPasswordAuditor.inspectNewData(userUpdateRequest, null, loc);

		if (response.isInvalid()) {
			InfoResponse infoResponse = new InfoResponse(HttpStatus.BAD_REQUEST,
					i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST, loc),
					i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST_EXT, loc));
			if (!response.isEmpty()) {
				infoResponse.setSubInfos(new ArrayList<AbstractSubInfoResponse>());
				response.getMessages()
						.forEach((k, v) -> infoResponse.getSubInfos().add(new ValidationInfoResponse(k, v)));
			}
			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());
		}
		UserUpdateDto userRegDto = ToUserUpdateDtoMapper.MAPPER
				.fromUserUpdateRequest(userUpdateRequest);

		userRegDto.setId(userId);

		log.info("IN updateUser - Perform update User");
		Optional<UserDto> userDtoO = userService.updateUserDto(userRegDto);

		if (userDtoO.isEmpty()) {
			log.info("IN updateUser - Update is fail. Inform to the registrant");
			InfoResponse infoResponse = new InfoResponse(HttpStatus.INTERNAL_SERVER_ERROR,
					i18n.getTranslation(RestV1Msg.USER_UPDATE_FAIL, loc),
					i18n.getTranslation(RestV1Msg.USER_UPDATE_FAIL_EXT, loc));

			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());

		}

		log.info("IN updateUser - Update is successful. Inform to the updater");
		UserRest userRest = UserRestMapper.MAPPER.toRest(userDtoO.get());
		return ResponseEntity.accepted().body(userRest);
	}

	/**
	 * Gets Info about current User.
	 *
	 * @param userPrincipal the UserPrincipal
	 * @param loc           the loc
	 * @return the current user
	 */
	@GetMapping(value = "/me", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DISPATCHER','ROLE_MANAGER','ROLE_PRODUCTOPER','ROLE_COURIER','ROLE_CLIENT','ROLE_USER')")
	public ResponseEntity<Object> getCurrentUser(@CurrentUser UserPrincipal userPrincipal, Locale loc) {
		log.debug("IN getCurrentUser - Get request about ME");
		Optional<UserDto> userDto = userService.fetchDtoById(userPrincipal.getId());

		if (userDto.isPresent()) {
			UserRest userRest = UserRestMapper.MAPPER.toRest(userDto.get());
			return ResponseEntity.ok(userRest);
		} else {
			InfoResponse infoResponse = new InfoResponse(HttpStatus.INTERNAL_SERVER_ERROR,
					i18n.getTranslation(RestV1Msg.INFO_INACESSIBLE, loc),
					i18n.getTranslation(RestV1Msg.INFO_INACESSIBLE_EXT, loc));

			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());
		}
	}

	/**
	 * Handle user dto audit exeption.
	 *
	 * @param ex      the UserDtoAuditExeption
	 * @param request the request
	 * @param loc     the Locale
	 * @return the response entity
	 */
	@ExceptionHandler(value = { UserDtoAuditExeption.class })
	@ResponseBody()
	public ResponseEntity<Object> handleUserDtoAuditExeption(UserDtoAuditExeption ex, WebRequest request, Locale loc) {
		log.debug("IN handleUserDtoAuditExeption - Gets exception: {}", ex.getMessage());

		InfoResponse infoResponse = new InfoResponse(HttpStatus.BAD_REQUEST,
				i18n.getTranslation(RestV1Msg.USER_NEW_CHECK_FAIL, loc),
				i18n.getTranslation(RestV1Msg.USER_NEW_CHECK_FAIL_EXT, loc));

		Multimap<String, String> response = ex.getAuditMessages();

		if (!response.isEmpty()) {
			infoResponse.setSubInfos(new ArrayList<AbstractSubInfoResponse>());
			response.forEach((k, v) -> infoResponse.getSubInfos().add(new ValidationInfoResponse(k, v)));
		}

		String headers = request.getHeader(HttpHeaders.ACCEPT);

		MediaType mt;
		if (headers.indexOf(MediaType.APPLICATION_JSON_VALUE) == -1) {
			mt = MediaType.APPLICATION_XML;
		} else {
			mt = MediaType.APPLICATION_JSON;
		}
		return ResponseEntity.status(infoResponse.getStatus()).contentType(mt).body(infoResponse);
	}

	/**
	 * Handle any unhandled exceptions.
	 *
	 * @param ex      the ex
	 * @param request the request
	 * @return the response entity
	 */
	@ExceptionHandler(value = { Exception.class })
	@ResponseBody()
	public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {
		log.debug("IN handleOtherExceptions - Gets exception: {}", ex.getMessage());

		InfoResponse infoResponse = new InfoResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);

		String headers = request.getHeader(HttpHeaders.ACCEPT);

		MediaType mt;
		if (headers.indexOf(MediaType.APPLICATION_JSON_VALUE) == -1) {
			mt = MediaType.APPLICATION_XML;
		} else {
			mt = MediaType.APPLICATION_JSON;
		}
		return ResponseEntity.status(infoResponse.getStatus()).contentType(mt).body(infoResponse);
	}

}
