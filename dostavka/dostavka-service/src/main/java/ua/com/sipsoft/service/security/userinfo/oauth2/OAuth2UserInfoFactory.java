package ua.com.sipsoft.service.security.userinfo.oauth2;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.service.security.OAuth2AuthenticationProcessingException;

/**
 * Each OAuth2 provider returns its JSON response when we receive authenticated
 * user data. The {@link OAuth2UserInfoFactory} class produces
 * {@link AbstractOAuth2UserInfo} depending on the provider ID.
 * 
 * @author Pavlo Degtyaryev
 */
@Slf4j
public class OAuth2UserInfoFactory {

	/**
	 * Gets the {@link AbstractOAuth2UserInfo} user info.
	 *
	 * @param the {@link String} registration id
	 * @param the {@link Map} of the attributes
	 * @return the {@link AbstractOAuth2UserInfo}
	 */
	public static AbstractOAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
		log.debug("Perform create AbstractOAuth2UserInfo by registrationId '{}' and attributes {}", registrationId, attributes);
		if (registrationId.equalsIgnoreCase("google")) {
			log.debug("Create Google User Info");
			return new GoogleOAuth2UserInfo(attributes);
		} else if (registrationId.equalsIgnoreCase("facebook")) {
			log.debug("Create Facebook User Info");
			return new FacebookOAuth2UserInfo(attributes);
		} else if (registrationId.equalsIgnoreCase("github")) {
			log.debug("Create GitHub User Info");
			return new GithubOAuth2UserInfo(attributes);
		} else {
			log.debug("Cannot support registrationId '{}'", registrationId);
			throw new OAuth2AuthenticationProcessingException(
					"Sorry! Login with " + registrationId + " is not supported yet.");
		}
	}
}
