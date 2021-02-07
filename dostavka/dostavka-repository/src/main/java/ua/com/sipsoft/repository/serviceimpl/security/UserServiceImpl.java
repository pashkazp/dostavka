package ua.com.sipsoft.repository.serviceimpl.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.common.VerificationToken;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.repository.serviceimpl.mapper.security.UserMapper;
import ua.com.sipsoft.repository.serviceimpl.mapper.security.UserRegistrationMapper;
import ua.com.sipsoft.repository.user.UserRepository;
import ua.com.sipsoft.service.dto.user.BaseUserInfoDto;
import ua.com.sipsoft.service.dto.user.UserDto;
import ua.com.sipsoft.service.dto.user.UserRegistrationDto;
import ua.com.sipsoft.service.dto.user.UserUpdateDto;
import ua.com.sipsoft.service.exception.ResourceNotFoundException;
import ua.com.sipsoft.service.exception.UserDtoAuditExeption;
import ua.com.sipsoft.service.security.OnRegistrationCompleteEvent;
import ua.com.sipsoft.service.security.UserPrincipal;
import ua.com.sipsoft.service.security.VerificationTokenService;
import ua.com.sipsoft.service.user.UserService;
import ua.com.sipsoft.service.util.EntityFilter;
import ua.com.sipsoft.service.util.HasFilteredList;
import ua.com.sipsoft.service.util.HasLimitedList;
import ua.com.sipsoft.service.util.HasPagingRequestToSortConvertor;
import ua.com.sipsoft.service.util.HasQueryToSortConvertor;
import ua.com.sipsoft.util.I18NProvider;
import ua.com.sipsoft.util.audit.AuditResponse;
import ua.com.sipsoft.util.audit.CreateRequestPropertyAuditor;
import ua.com.sipsoft.util.audit.UpdateRequestPropertyAuditor;
import ua.com.sipsoft.util.message.LoginMsg;
import ua.com.sipsoft.util.message.RestV1Msg;
import ua.com.sipsoft.util.message.UserEntityCheckMsg;
import ua.com.sipsoft.util.paging.Page;
import ua.com.sipsoft.util.paging.PagingRequest;
import ua.com.sipsoft.util.query.Query;
import ua.com.sipsoft.util.security.AgreedEmailCheck;
import ua.com.sipsoft.util.security.AgreedPasswordCheck;
import ua.com.sipsoft.util.security.AgreedUsernameCheck;
import ua.com.sipsoft.util.security.Role;
import ua.com.sipsoft.util.security.VerificationTokenType;

/**
 * The Class UserServiceImpl.
 *
 * @author Pavlo Degtyaryev
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl
		implements UserService, HasQueryToSortConvertor, HasPagingRequestToSortConvertor, HasFilteredList,
		HasLimitedList, CreateRequestPropertyAuditor<UserRegistrationDto>,
		UpdateRequestPropertyAuditor<UserUpdateDto> {

	/** The dao. */
	private final UserRepository dao;

	/** The verification token service. */
	private final VerificationTokenService verificationTokenService;

	private final ApplicationEventPublisher eventPublisher;

	private final PasswordEncoder passwordEncoder;

	private final I18NProvider i18n;
	private final UserMapper userMapper;
	private final UserRegistrationMapper userRegistrationMapper;

	/**
	 * Save user.
	 *
	 * @param user the user
	 * @return the user
	 */
	@Override
	public User saveUser(User user) {
		log.info("Perform Save user: \"{}\"", user);
		if (user == null) {
			log.warn("User saving impossible. Missing some data. ");
			return null;
		}
		try {
			return dao.save(user);
		} catch (Exception e) {
			log.warn(e.getMessage());
			return null;
		}
	}

	/**
	 * Fetch by id.
	 *
	 * @param id the id
	 * @return the optional
	 */
	@Override
	public Optional<User> fetchById(Long id) {
		log.info("Perform get user by Id: \"{}\"", id);
		if (id == null) {
			log.warn("User fetching impossible. Missing some data. ");
		}
		try {
			return dao.findById(id);
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
		return Optional.empty();
	}

	/**
	 * Gets the by roles.
	 *
	 * @param roles the roles
	 * @return the by roles
	 */
	@Override
	public List<User> getByRoles(Collection<Role> roles) {
		log.info("Get users by Roles collection: \"{}\"", roles);
		if (roles == null) {
			log.warn("Users fetching impossible. Missing some data. ");
		}
		try {
			return dao.getByRoles(roles);
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
		return Collections.emptyList();
	}

	/**
	 * Gets the by roles and find by name.
	 *
	 * @param roles the roles
	 * @param name  the name
	 * @return the by roles and find by name
	 */
	@Override
	public List<User> getByRolesAndFindByName(Collection<Role> roles, String name) {
		log.info("Get Users by roles \"{}\" and by name: \"{}\"", roles, name);
		if (roles == null || name == null) {
			log.warn("Users fetching impossible. Missing some data. ");
		}
		try {
			return dao.getByRolesAndFindByName(roles, name.toLowerCase());
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
		return Collections.emptyList();
	}

	/**
	 * Save users.
	 *
	 * @param users the users
	 * @return the collection
	 */
	@Override
	public Collection<User> saveUsers(Collection<User> users) {
		// TODO checkeing
		users = dao.saveAll(users);
		dao.flush();
		return users;
	}

	/**
	 * Gets the queried usersby filter.
	 *
	 * @param query the query
	 * @return the queried usersby filter
	 */
	@Override
	public Stream<User> getQueriedUsersbyFilter(Query<User, EntityFilter<User>> query) {
		log.debug(
				"Get requested page Users with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
				query.getOffset(), query.getLimit(), query.getSortOrders(), query.getFilter().get().toString());
		try {
			return dao.findAll(queryToSort(query))
					.stream()
					.filter(entity -> query.getFilter().get().isPass(entity))
					.skip(query.getOffset())
					.limit(query.getLimit());
		} catch (Exception e) {
			log.error("The Courier Requests list was not received for a reason: {}", e.getMessage());
		}
		return Stream.empty();
	}

	/**
	 * Gets the queried users by filter count.
	 *
	 * @param query the query
	 * @return the queried users by filter count
	 */
	@Override
	public int getQueriedUsersByFilterCount(Query<User, EntityFilter<User>> query) {
		log.debug("Get requested size Users with filter '{}'", query.getFilter().get().toString());
		return (int) getQueriedUsersbyFilter(query).count();
	}

	/**
	 * Gets the queried usersby filter.
	 *
	 * @param query      the query
	 * @param facilityId the facility id
	 * @return the queried usersby filter
	 */
	@Override
	public Stream<User> getQueriedUsersByFacilityIdByFilter(Query<User, EntityFilter<User>> query, Long facilityId) {
		log.debug(
				"Get requested page Users with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
				query.getOffset(), query.getLimit(), query.getSortOrders(), query.getFilter().get().toString());
		try {
			return dao.getUsersByFasilityId(facilityId, queryToSort(query))
					.stream()
					.filter(entity -> query.getFilter().get().isPass(entity))
					.skip(query.getOffset())
					.limit(query.getLimit());
		} catch (Exception e) {
			log.error("The Courier Requests list was not received for a reason: {}", e.getMessage());
		}
		return Stream.empty();
	}

	/**
	 * Gets the queried users by filter count.
	 *
	 * @param query      the query
	 * @param facilityId the facility id
	 * @return the queried users by filter count
	 */
	@Override
	public int getQueriedUsersByFacilityIdByFilterCount(Query<User, EntityFilter<User>> query, Long facilityId) {
		log.debug("Get requested size Users with filter '{}'", query.getFilter().get().toString());
		return (int) getQueriedUsersByFacilityIdByFilter(query, facilityId).count();
	}

	/**
	 * Fetch by email.
	 *
	 * @param email the email
	 * @return the optional
	 */
	@Override
	public Optional<User> fetchByEmail(String email) {
		if (StringUtils.isBlank(email)) {
			return Optional.empty();
		}
		try {
			return dao.findByEmailIgnoreCase(email);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	/**
	 * Register new user.
	 *
	 * @param userDTO the user DTO
	 * @return the optional
	 */
	@Override
	public Optional<UserDto> registerNewUser(UserRegistrationDto userDTO) {
		log.info("Register new User \"{}\"", userDTO);
		return registerNewUser(userDTO, true);
	}

	/**
	 * Register new user.
	 *
	 * @param userRegistrationDto the user DTO
	 * @return the optional
	 */
	@Override
	public Optional<UserDto> registerNewUser(@NonNull UserRegistrationDto userRegistrationDto,
			Boolean isSendRegistrationEmail) {
		log.info("Register new User \"{}\". Send registration email: {}", userRegistrationDto, isSendRegistrationEmail);

		Locale loc = LocaleContextHolder.getLocale();
		AuditResponse response = inspectNewData(userRegistrationDto, null, loc);

		if (response.isInvalid()) { // if there are any errors
			log.info("IN registerNewUser - Registration is fail. Inform to the registrant");

			UserDtoAuditExeption ex = new UserDtoAuditExeption(response);
			ex.setErrMsg(i18n.getTranslation(RestV1Msg.USER_NEW_CHECK_FAIL, loc));
			ex.setErrMsgExt(i18n.getTranslation(RestV1Msg.USER_NEW_CHECK_FAIL_EXT, loc));
			throw ex;
		}

		if (userRegistrationDto == null || StringUtils.isBlank(userRegistrationDto.getName())) {
			log.warn("New User Creation is impossible. Missing some data. ");
			return Optional.empty();
		}

		try {
			Optional<User> userO = dao.findByEmailIgnoreCase(userRegistrationDto.getEmail());
			if (userO.isPresent()) {
				return Optional.empty();
			}

			User user = userRegistrationMapper.fromDto(userRegistrationDto);

			user.setPassword(passwordEncoder.encode(user.getPassword()));

			user = dao.save(user);

			UserDto userDto = userMapper.toDto(user);
			if (isSendRegistrationEmail) {
				eventPublisher.publishEvent(
						new OnRegistrationCompleteEvent(user, LocaleContextHolder.getLocale()));
			}
			return Optional.ofNullable(userDto);
		} catch (Exception e) {
			log.warn(e.getMessage());
			return Optional.empty();
		}
	}

	@Override
	public Optional<UserDto> updateUserDto(@NonNull UserUpdateDto userUpdDto) {

		Locale loc = LocaleContextHolder.getLocale();
		AuditResponse response = inspectUpdatedData(userUpdDto, null, loc);

		if (response.isInvalid()) {
			log.info("IN updateUserDto - User update is fail. Inform to the updater");

			UserDtoAuditExeption ex = new UserDtoAuditExeption(response);
			ex.setErrMsg(i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST, loc));
			ex.setErrMsgExt(i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST_EXT, loc));
			throw ex;

		}

		Optional<User> userO = fetchById(userUpdDto.getId());
		if (userO.isEmpty()) {
			ResourceNotFoundException ex = new ResourceNotFoundException("User", "id", userUpdDto.getId());
			throw ex;
		}

		User user = userO.get();

		if (StringUtils.isNotBlank(userUpdDto.getName())) {
			user.setName(userUpdDto.getName());
		}
		if (StringUtils.isNotBlank(userUpdDto.getEmail())) {
			user.setEmail(userUpdDto.getEmail());
		}
		if (StringUtils.isNotBlank(userUpdDto.getPassword())) {
			user.setPassword(userUpdDto.getPassword());
		}

		if (userUpdDto.getRoles() != null && !userUpdDto.getRoles().isEmpty()) {
			user.addRoles(userUpdDto.getRoles());
		}

		if (userUpdDto.getVerified() != null) {
			user.setVerified(userUpdDto.getVerified());
		}
		if (userUpdDto.getEnabled() != null) {
			user.setEnabled(userUpdDto.getEnabled());
		}

		user = dao.saveAndFlush(user);
		return Optional.ofNullable(userMapper.toDto(user));
	}

	/**
	 * Creates the verification token.
	 *
	 * @param user      the user
	 * @param token     the token
	 * @param tokenType the token type
	 * @return the verification token
	 */
	@Override
	public VerificationToken createVerificationToken(@NonNull User user, @NonNull String token,
			VerificationTokenType tokenType) {
		log.info("Creation verification token \"{}\" for new user: \"{}\"", token, user);
		if (user == null || token == null || token.isEmpty()) {
			log.warn("Creation verification token impossible. Missing some data. ");
			return null;
		}
		try {
			VerificationToken vToken = new VerificationToken(user, token, tokenType);
			return verificationTokenService.saveVerificationToken(vToken);
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
		return null;

	}

	@Override
	public Page<UserDto> getFilteredPage(@NonNull PagingRequest pagingRequest,
			@NonNull EntityFilter<User> entityFilter) {
		log.debug(
				"Get requested page Users with PagingRequest '{}' and EntityFilter<User> '{}'", pagingRequest,
				entityFilter);

		Page<UserDto> page = new Page<>();
		List<User> users = dao.findAll(toSort(pagingRequest));

		page.setRecordsTotal(users.size());

		users = getFiteredList(users, entityFilter);

		page.setRecordsFiltered(users.size());

		users = getLimitedList(users, pagingRequest.getStart(), pagingRequest.getLength());

		page.setData(userMapper.toDto(users));

		page.setDraw(pagingRequest.getDraw());

		return page;
	}

	@Override
	public Optional<UserRegistrationDto> fetchRegistrationDtoById(long id) {
		Optional<UserRegistrationDto> userDto;
		Optional<User> user = fetchById(id);
		if (user.isPresent()) {
			userDto = Optional.ofNullable(userRegistrationMapper.toDto(user.get()));
		} else {
			userDto = Optional.empty();
		}
		return userDto;
	}

	@Override
	public List<UserDto> getAllUsersDto() {
		List<User> users = dao.findAll();
		List<UserDto> usersDto = userMapper.toDto(users);
		return usersDto;
	}

	@Override
	public Optional<UserDto> fetchDtoById(@NonNull Long id) {
		Optional<UserDto> userDto;
		Optional<User> user = fetchById(id);
		if (user.isPresent()) {
			userDto = Optional.ofNullable(userMapper.toDto(user.get()));
		} else {
			userDto = Optional.empty();
		}
		return userDto;
	}

	@Override
	public Optional<Long> fetchIdByEmail(@NonNull String email) {
		Optional<User> userO = fetchByEmail(email);
		if (userO.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.ofNullable(userO.get().getId());
		}
	}

	@Override
	public List<User> getAllManagers() {
		List<User> users = dao.getByRoles(Collections.singletonList(Role.ROLE_MANAGER));
		return users;
	}

	@Override
	public List<UserDto> getAllManagersDto() {
		List<UserDto> usersDto = userMapper.toDto(getAllManagers());
		return usersDto;
	}

	@Override
	public boolean isEmailTaken(String email) {
		// TODO Auto-generated method stub
		return fetchByEmail(email).isPresent();
	}

	@Override
	public boolean isEmailTakenByOtherId(String email, Long id) {
		Optional<User> u = fetchByEmail(email);
		if (u.isPresent() && !u.get().getId().equals(id) &&
				StringUtils.equalsIgnoreCase(email, u.get().getEmail())) {
			return true;
		}
		return false;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("Perform to load User by Email '{}'", username);
		User user = fetchByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + username));

		return new UserPrincipal(user);
	}

	/**
	 * Load user by User Id.
	 *
	 * @param id the User Id
	 * @return the user details
	 * @throws UsernameNotFoundException the user not found exception
	 */
	@Override
	public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
		log.debug("Perform to load User by Id '{}'", id);
		User user = fetchById(id).orElseThrow(
				() -> new UsernameNotFoundException("User not found with Id : " + id));

		return new UserPrincipal(user);
	}

	// quality check of parameters
	private AuditResponse performBaseUserDtoCheck(@NonNull BaseUserInfoDto userDto,
			AuditResponse result,
			Locale loc) {
		if (loc == null) {
			loc = LocaleContextHolder.getLocale();
		}

		if (result == null) {
			result = new AuditResponse();
		}

		if (StringUtils.isNotBlank(userDto.getName())) {
			if (!AgreedUsernameCheck.agreedUsernameCheck(userDto.getName())) {
				log.info("IN performBaseUserDtoCheck - Users data properties was rejected. User name '{}' is bad.",
						userDto.getName());
				result.setValid(false);
				result.addMessage("name", i18n.getTranslation(UserEntityCheckMsg.NAME_CHR, loc));
			}
		}

		if (StringUtils.isNotBlank(userDto.getEmail())) {
			if (!AgreedEmailCheck.agreedEmailCheck(userDto.getEmail())) {
				log.info("IN performBaseUserDtoCheck - Users data properties was rejected. Email '{}' is bad.",
						userDto.getEmail());
				result.setValid(false);
				result.addMessage("email", i18n.getTranslation(UserEntityCheckMsg.EMAIL_CHR, loc));
			}
		}

		if (StringUtils.isNotBlank(userDto.getPassword())) {
			if (!AgreedPasswordCheck.adreedPasswordCheck(userDto.getPassword())) {
				log.info("IN performUpdatedUserDtoCheck - Users data properties was rejected. Password is bad");
				result.setValid(false);
				result.addMessage("password", i18n.getTranslation(UserEntityCheckMsg.PASS_CHR, loc));
			}
		}

		return result;
	}

	@Override
	public AuditResponse inspectUpdatedData(UserUpdateDto inspectedInfo, AuditResponse result, Locale loc) {
		if (loc == null) {
			loc = LocaleContextHolder.getLocale();
		}
		result = performBaseUserDtoCheck(inspectedInfo, result, loc);

		if (result.isValid() && StringUtils.isNotBlank(inspectedInfo.getEmail())) { // if there are no any errors

			log.info("IN inspectUpdatingInfo - Request validation is fail. Inform to the registrant");

			if (!isEmailTakenByOtherId(inspectedInfo.getEmail(), inspectedInfo.getId())) {
				return result;
			}
			result.addMessage("email", i18n.getTranslation(UserEntityCheckMsg.EMAIL_TAKE_OTHER, loc));
		}

		result.setValid(result.getMessages().isEmpty());

		return result;
	}

	@Override
	public AuditResponse inspectNewData(UserRegistrationDto inspectedInfo, AuditResponse result, Locale loc) {
		if (loc == null) {
			loc = LocaleContextHolder.getLocale();
		}
		result = performBaseUserDtoCheck(inspectedInfo, result, loc);

		if (StringUtils.isBlank(inspectedInfo.getPassword())) {
			log.info("IN inspectGeneratingInfo - New User data properties was rejected. Password is expected");
			result.addMessage("password", i18n.getTranslation(UserEntityCheckMsg.PASS_EXPECT, loc));
		}
		if (StringUtils.isBlank(inspectedInfo.getName())) {
			log.info("IN inspectGeneratingInfo - New User data properties was rejected. Name is expected");
			result.addMessage("name", i18n.getTranslation(UserEntityCheckMsg.SMALL_NAME, loc));
		}

		if (StringUtils.isBlank(inspectedInfo.getEmail())) {
			log.info("IN inspectGeneratingInfo - New User data properties was rejected. Email is expected");
			result.addMessage("email", i18n.getTranslation(UserEntityCheckMsg.EMAIL_EXPECT, loc));
		} else {
			if (result.isEmpty()) {
				log.info("IN inspectGeneratingInfo - Perform search already registered User by Emal '{}'",
						inspectedInfo.getEmail());
				if (isEmailTaken(inspectedInfo.getEmail())) {
					log.info(
							"IN inspectGeneratingInfo - New Users data properties was rejected. Email '{}' is already taken.",
							inspectedInfo.getEmail());
					result.addMessage("email", i18n.getTranslation(LoginMsg.SIGNUP_EMAIL_TAKEN, loc));
				}
			}
		}

		result.setValid(result.getMessages().isEmpty());

		return result;
	}

}