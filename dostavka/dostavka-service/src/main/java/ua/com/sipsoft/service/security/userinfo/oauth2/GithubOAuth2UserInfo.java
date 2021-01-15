package ua.com.sipsoft.service.security.userinfo.oauth2;

import java.util.Map;

/**
 * Cast the {@link GithubOAuth2UserInfo} class as {@link AbstractOAuth2UserInfo} class.
 * 
 * @author Pavlo Degtyaryev
 */
public class GithubOAuth2UserInfo extends AbstractOAuth2UserInfo {

	/**
	 * Instantiates a new Github AbstractOAuth2UserInfo class realization.
	 *
	 * @param {@link Map} the attributes
	 */
	public GithubOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	/**
	 * Gets the id attribute as OAuth2 user Id.
	 *
	 * @return the {@link String} id
	 */
	@Override
	public String getId() {
		return ((Integer) attributes.get("id")).toString();
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
	 * Gets the avatar_url attribute as OAuth2 user Image Url.
	 *
	 * @return the {@link String} image url
	 */
	@Override
	public String getImageUrl() {
		return (String) attributes.get("avatar_url");
	}
}
