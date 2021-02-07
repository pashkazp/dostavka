package ua.com.sipsoft.service.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.util.security.Role;

/**
 * The UserPrincipal class represents an authenticated Spring Security
 * principal. It contains the details of the authenticated user
 *
 * @author Pavlo Degtyaryev
 */
@Slf4j
public class UserPrincipal implements UserDetails, OidcUser {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -477179629478050549L;

	/** The user. */
	private final User user;

	/** The attributes. */
	private Map<String, Object> attributes;

	private OidcIdToken idToken;

	/**
	 * Instantiates a new user principal.
	 *
	 * @param user the user
	 */
	public UserPrincipal(User user) {
		log.debug("create UserPrincipal from User");
		this.user = new User(user);
		// this.user = UserMapper.MAPPER.getCopy(user);
		this.user.setPassword(user.getPassword());
	}

	/**
	 * Instantiates a new user principal.
	 *
	 * @param User        the user
	 * @param Map<String, Object> the attributes
	 */
	public UserPrincipal(User user, Map<String, Object> attributes) {
		this(user);
		log.debug("create UserPrincipal from User and attributes");
		setAttributes(attributes);
	}

	public UserPrincipal(User user, OidcUser oUser) {
		this(user);
		log.debug("create UserPrincipal from User and OidcUser");
		setAttributes(oUser.getClaims());
		this.idToken = oUser.getIdToken();
	}

	/**
	 * Gets Collection of the authorities.
	 *
	 * @return List<GrantedAuthority> the authorities
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getRoleName()))
				.collect(Collectors.toList());
	}

	/**
	 * Gets the User id.
	 *
	 * @return Long the User id
	 */
	public Long getId() {
		return user.getId();
	}

	/**
	 * Gets the User password.
	 *
	 * @return the User password
	 */
	@Override
	@JsonIgnore
	public String getPassword() {
		return user.getPassword();
	}

	/**
	 * Gets the User username.
	 *
	 * @return User the username
	 */
	@Override
	public String getUsername() {

		return user.getEmail();
	}

	/**
	 * Gets the User email.
	 *
	 * @return the User email
	 */
	@Override
	public String getEmail() {
		return user.getEmail();
	}

	/**
	 * Checks if is account non expired.
	 *
	 * @return true, if is account non expired
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * Checks if is account non locked.
	 *
	 * @return true, if is account non locked
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * Checks if is credentials non expired.
	 *
	 * @return true, if is credentials non expired
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
	@Override
	public boolean isEnabled() {
		return (user.getEnabled() && user.getVerified());
	}

	/**
	 * Gets the User roles.
	 *
	 * @return the Collection<Role> User roles
	 */
	public Collection<Role> getRoles() {
		return Collections.unmodifiableSet(user.getRoles());
	}

	/**
	 * Gets the highest role. Where Admin - highest and User - lowest. If Role is
	 * absent return User
	 *
	 * @return the highest {@link Role}
	 */
	public Role getHighestRole() {
		return user.getHighesRole();
	}

	/**
	 * Gets the User copy.
	 *
	 * @return the User copy
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Gets the attributes.
	 *
	 * @return the attributes
	 */
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	/**
	 * Sets the attributes.
	 *
	 * @param attributes the attributes
	 */
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Gets the User name.
	 *
	 * @return the User name
	 */
	@Override
	public String getName() {
		return this.user.getName();
	}

	@Override
	public Map<String, Object> getClaims() {
		return this.attributes;
	}

	@Override
	public OidcUserInfo getUserInfo() {
		return new OidcUserInfo(this.attributes);
	}

	@Override
	public OidcIdToken getIdToken() {
		return this.idToken;
	}

}