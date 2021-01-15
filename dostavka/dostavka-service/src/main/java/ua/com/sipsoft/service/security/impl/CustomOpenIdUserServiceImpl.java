package ua.com.sipsoft.service.security.impl;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.security.OpenId2AuthenticationProcessingException;
import ua.com.sipsoft.service.security.UserPrincipal;
import ua.com.sipsoft.service.security.userinfo.openid.GoogleOpenId2UserInfo;
import ua.com.sipsoft.service.user.UserService;
import ua.com.sipsoft.util.security.Role;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOpenIdUserServiceImpl extends OidcUserService {
	/** The {@link UserService} interface. */
	private final UserService userService;

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		log.debug(userRequest.toString());
		OidcUser oidcUser = super.loadUser(userRequest);

		try {
			return processOidc2User(oidcUser);
		} catch (AuthenticationException ex) {
			log.debug("AuthenticationException thrown");
			throw ex;
		} catch (Exception ex) {
			// Throwing an instance of AuthenticationException will trigger the
			// OpenId2AuthenticationFailureHandler
			log.debug("InternalAuthenticationServiceException thrown for trigger OpenId2AuthenticationFailureHandler");
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
		}

	}

	private OidcUser processOidc2User(OidcUser oidcUser) {
		log.debug("Create GoogleOpenId2UserInfo");
		GoogleOpenId2UserInfo userInfo = new GoogleOpenId2UserInfo(oidcUser.getClaims());
		if (StringUtils.isEmpty(userInfo.getEmail())) {
			log.debug("User Email is empty");
			throw new OpenId2AuthenticationProcessingException("Email not found from OAuth2 provider");
		}

		log.debug("Perform to load user by UserName");
		Optional<User> userOptional = userService.fetchByEmail(userInfo.getEmail());
		User user;
		if (userOptional.isPresent()) {
			log.debug("Stored user exist");
			user = userOptional.get();
			log.debug("User exist. Update info {}.", userInfo);
			user = updateExistingUser(user, userInfo);
		} else {
			log.debug("User not exist. Create new User with GoogleOpenId2UserInfo {}.", userInfo);
			user = registerNewUser(userInfo);
		}
		log.debug("Return new Principal for User {}", user);
		return new UserPrincipal(user, oidcUser);
	}

	private User registerNewUser(GoogleOpenId2UserInfo userInfo) {
		log.debug("Register New User with User Info {}", userInfo);
		User user = new User();

		user.setName(userInfo.getName());
		user.setEmail(userInfo.getEmail());
		user.setEnabled(true);
		user.setVerified(true);
		user.addRoles(Role.ROLE_USER);
		return userService.saveUser(user);
	}

	private User updateExistingUser(User existingUser, GoogleOpenId2UserInfo userInfo) {
		log.debug("Update UserInfo of {} to {}", existingUser.getName(), userInfo);
		existingUser.setName(userInfo.getName());
		return userService.saveUser(existingUser);
	}

}
