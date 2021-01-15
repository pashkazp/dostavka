package ua.com.sipsoft.service.security.userinfo.oauth2;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * Cast the {@link FacebookOAuth2UserInfo} class as {@link AbstractOAuth2UserInfo}
 * class.
 * 
 * @author Pavlo Degtyaryev
 */
@Slf4j
public class FacebookOAuth2UserInfo extends AbstractOAuth2UserInfo {

	/**
	 * Instantiates a new Facebook AbstractOAuth2UserInfo class realization.
	 *
	 * @param {@link Map} the attributes
	 */
	public FacebookOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
		log.debug("Instantiate Facebook user");
	}

	/**
	 * Gets the id attribute as OAuth2 user Id.
	 *
	 * @return the id
	 */
	@Override
	public String getId() {
		return (String) attributes.get("id");
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
	 * Gets the picture and data attributes as OAuth2 user Image Url.
	 *
	 * @return the {@link String} image url
	 */
	@Override
	public String getImageUrl() {
		if (attributes.containsKey("picture")) {
			Map<String, Object> pictureObj = (Map<String, Object>) attributes.get("picture");
			if (pictureObj.containsKey("data")) {
				Map<String, Object> dataObj = (Map<String, Object>) pictureObj.get("data");
				if (dataObj.containsKey("url")) {
					return (String) dataObj.get("url");
				}
			}
		}
		return null;
	}
}
