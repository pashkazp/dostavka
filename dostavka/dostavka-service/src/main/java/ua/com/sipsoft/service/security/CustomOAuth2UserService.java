package ua.com.sipsoft.service.security;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * The interface CustomOAuth2UserService represent class that store and retrieve
 * {@link OAuth2User}.
 *
 * @author Pavlo Degtyaryev
 */
@Service
public interface CustomOAuth2UserService {

	/**
	 * Perform to load the {@link OAuth2User} user.
	 *
	 * @param the {@link OAuth2UserRequest}
	 * @return the o{@link OAuth2User}
	 * @throws the {@link OAuth2AuthenticationException}
	 */
	OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException;

}