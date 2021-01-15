package ua.com.sipsoft.service.security.impl;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.security.CustomOAuth2UserService;
import ua.com.sipsoft.service.security.OAuth2AuthenticationProcessingException;
import ua.com.sipsoft.service.security.UserPrincipal;
import ua.com.sipsoft.service.security.userinfo.oauth2.AbstractOAuth2UserInfo;
import ua.com.sipsoft.service.security.userinfo.oauth2.OAuth2UserInfoFactory;
import ua.com.sipsoft.service.user.UserService;
import ua.com.sipsoft.util.security.Role;

/**
 * Perform CRUD operations with {@link OAuth2User} on the {@link UserService}
 * service.
 * 
 * @author Pavlo Degtyaryov
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserServiceImpl extends DefaultOAuth2UserService implements CustomOAuth2UserService {

	/** The {@link UserService} interface. */
	private final UserService userService;

	/**
	 * Perform to load the {@link OAuth2User} user.
	 *
	 * @param the {@link OAuth2UserRequest}
	 * @return the o{@link OAuth2User}
	 * @throws the {@link OAuth2AuthenticationException}
	 */
	@Override
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {

		log.debug("Perform to load OAuth2User by OAuth2UserRequest: '{}'", oAuth2UserRequest);
		OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

		try {
			return processOAuth2User(oAuth2UserRequest, oAuth2User);
		} catch (AuthenticationException ex) {
			log.debug("AuthenticationException thrown");
			throw ex;
		} catch (Exception ex) {
			// Throwing an instance of AuthenticationException will trigger the
			// OAuth2AuthenticationFailureHandler
			log.debug("InternalAuthenticationServiceException thrown for trigger OAuth2AuthenticationFailureHandler");
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
		}
	}

	/**
	 * Produce the {@link AbstractOAuth2UserInfo} from the {@link OAuth2UserRequest} using
	 * {@link OAuth2UserInfoFactory} and perform to store new or update existing
	 * {@link User}. Return the {@link UserPrincipal} of stored {@link OAuth2User}
	 *
	 * @param the {@link OAuth2UserRequest}
	 * @param the {@link OAuth2User}
	 * @return the {@link OAuth2User}
	 */
	private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {

		log.debug("Create AbstractOAuth2UserInfo");
		AbstractOAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
				oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
		if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
			log.debug("User Email is empty");
			throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
		}

		log.debug("Perform to load user by UserName");
		Optional<User> userOptional = userService.fetchByEmail(oAuth2UserInfo.getEmail());
		User user;
		if (userOptional.isPresent()) {
			log.debug("Stored user exist");
			user = userOptional.get();
			log.debug("User exist. Update info {}.", oAuth2UserInfo);
			user = updateExistingUser(user, oAuth2UserInfo);
		} else {
			log.debug("User not exist. Create new User with oAuth2UserInfo {}.", oAuth2UserInfo);
			user = registerNewUser(oAuth2UserInfo);
		}

		log.debug("Return new Principal for User {}", user);
		return new UserPrincipal(user, oAuth2User.getAttributes());
	}

	/**
	 * Register new {@link User} using information about {@link OAuthProvider} from
	 * {@link OAuth2UserRequest} and {@link AbstractOAuth2UserInfo}.
	 *
	 * @param the {@link OAuth2UserRequest}
	 * @param the {@link AbstractOAuth2UserInfo}
	 * @return the new {@link User}
	 */
	private User registerNewUser(AbstractOAuth2UserInfo oAuth2UserInfo) {

		log.debug("Register New User with User Info {}", oAuth2UserInfo);
		User user = new User();

		user.setName(oAuth2UserInfo.getName());
		user.setEmail(oAuth2UserInfo.getEmail());
		user.setEnabled(true);
		user.setVerified(true);
		user.addRoles(Role.ROLE_USER);
		return userService.saveUser(user);
	}

	/**
	 * Update existing {@link User} using {@link AbstractOAuth2UserInfo}.
	 *
	 * @param the existing {@link User}
	 * @param the user info {@link AbstractOAuth2UserInfo}
	 * @return the updated {@link User}
	 */
	private User updateExistingUser(User existingUser, AbstractOAuth2UserInfo oAuth2UserInfo) {
		log.debug("Update UserInfo of {} to {}", existingUser.getName(), oAuth2UserInfo);
		existingUser.setName(oAuth2UserInfo.getName());
		return userService.saveUser(existingUser);
	}
}
