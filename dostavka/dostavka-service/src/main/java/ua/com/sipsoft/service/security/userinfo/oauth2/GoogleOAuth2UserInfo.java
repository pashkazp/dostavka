package ua.com.sipsoft.service.security.userinfo.oauth2;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * Cast the {@link GoogleOAuth2UserInfo} class as {@link AbstractOAuth2UserInfo} class.
 * 
 * @author Pavlo Degtyaryev
 */
@Slf4j
public class GoogleOAuth2UserInfo extends AbstractOAuth2UserInfo {

	/**
	 * Instantiates a new Google AbstractOAuth2UserInfo class realization.
	 *
	 * @param {@link Map} the attributes
	 */
	public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
		log.debug("Instantiate Google OAuth user");
	}

	/**
	 * Gets the sub attribute as OAuth2 user Id.
	 *
	 * @return the {@link String} id
	 */
	@Override
	public String getId() {
		return (String) attributes.get("sub");
	}

	/**
	 * Gets the name attribute as OAuth2 user Name.
	 *
	 * @return the {@link String} name
	 */
	@Override
	public String getName() {
		return (String) attributes.get("name");
	}

	/**
	 * Gets the Email attribute as OAuth2 user Email.
	 *
	 * @return the {@link String} email
	 */
	@Override
	public String getEmail() {
		return (String) attributes.get("email");
	}

	/**
	 * Gets the picture attribute as OAuth2 user Image Url.
	 *
	 * @return the {@link String} image url
	 */
	@Override
	public String getImageUrl() {
		return (String) attributes.get("picture");
	}
}
