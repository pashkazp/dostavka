package ua.com.sipsoft.service.user;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ua.com.sipsoft.dao.common.VerificationToken;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.dto.user.UserDto;
import ua.com.sipsoft.service.dto.user.UserRegistrationDto;
import ua.com.sipsoft.service.dto.user.UserUpdateDto;
import ua.com.sipsoft.service.util.EntityFilter;
import ua.com.sipsoft.util.paging.Page;
import ua.com.sipsoft.util.paging.PagingRequest;
import ua.com.sipsoft.util.query.Query;
import ua.com.sipsoft.util.security.Role;
import ua.com.sipsoft.util.security.VerificationTokenType;

/**
 * The Interface UserService.
 *
 * @author Pavlo Degtyaryev
 */
@Service
public interface UserService extends UserDetailsService {

	/**
	 * Save user.
	 *
	 * @param user the user
	 * @return the user
	 */
	User saveUser(User user);

	/**
	 * Save users.
	 *
	 * @param users the users
	 * @return the collection
	 */
	Collection<User> saveUsers(Collection<User> users);

	/**
	 * Fetch by id.
	 *
	 * @param id the id
	 * @return the optional
	 */
	Optional<User> fetchById(Long id);

	/**
	 * Gets the by roles.
	 *
	 * @param roles the roles
	 * @return the by roles
	 */
	List<User> getByRoles(Collection<Role> roles);

	/**
	 * Gets the by roles and find by name.
	 *
	 * @param roles the roles
	 * @param name  the name
	 * @return the by roles and find by name
	 */
	List<User> getByRolesAndFindByName(Collection<Role> roles, String name);

	/**
	 * Gets the queried usersby filter.
	 *
	 * @param query the query
	 * @return the queried usersby filter
	 */
	Stream<User> getQueriedUsersbyFilter(Query<User, EntityFilter<User>> query);

	/**
	 * Gets the queried users by filter count.
	 *
	 * @param query the query
	 * @return the queried users by filter count
	 */
	int getQueriedUsersByFilterCount(Query<User, EntityFilter<User>> query);

	/**
	 * Gets the queried usersby filter.
	 *
	 * @param query      the query
	 * @param facilityId the facility id
	 * @return the queried usersby filter
	 */
	Stream<User> getQueriedUsersByFacilityIdByFilter(Query<User, EntityFilter<User>> query, Long facilityId);

	/**
	 * Gets the queried users by filter count.
	 *
	 * @param query      the query
	 * @param facilityId the facility id
	 * @return the queried users by filter count
	 */
	int getQueriedUsersByFacilityIdByFilterCount(Query<User, EntityFilter<User>> query, Long facilityId);

	/**
	 * Fetch by email.
	 *
	 * @param email the email
	 * @return the optional
	 */
	Optional<User> fetchByEmail(String email);

	boolean isEmailTaken(String email);

	/**
	 * Register new user.
	 *
	 * @param user the user
	 * @return the optional
	 */
	Optional<UserDto> registerNewUser(UserRegistrationDto user);

	/**
	 * Register new user.
	 *
	 * @param user the user
	 * @return the optional
	 */
	Optional<UserDto> registerNewUser(UserRegistrationDto user, Boolean sendRegistrationEmail);

	/**
	 * Creates the verification token.
	 *
	 * @param user      the user
	 * @param token     the token
	 * @param tokenType the token type
	 * @return the verification token
	 */
	VerificationToken createVerificationToken(User user, String token, VerificationTokenType tokenType);

	Page<UserDto> getFilteredPage(PagingRequest pagingRequest, EntityFilter<User> userFilter);

	Optional<UserRegistrationDto> fetchRegistrationDtoById(long id);

	List<UserDto> getAllUsersDto();

	Optional<UserDto> fetchDtoById(Long id);

	Optional<UserDto> updateUserDto(UserUpdateDto userDto);

	Optional<Long> fetchIdByEmail(String email);

	List<User> getAllManagers();

	List<UserDto> getAllManagersDto();

	boolean isEmailTakenByOtherId(String email, Long id);

	/**
	 * Load user by User Id.
	 *
	 * @param id the User Id
	 * @return the user details
	 * @throws UsernameNotFoundException the user not found exception
	 */
	UserDetails loadUserById(Long id) throws UsernameNotFoundException;

}