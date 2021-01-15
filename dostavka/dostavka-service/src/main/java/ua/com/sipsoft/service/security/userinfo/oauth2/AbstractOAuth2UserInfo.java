package ua.com.sipsoft.service.security.userinfo.oauth2;

import java.util.Map;

/**
 * The abstract Class AbstractOAuth2UserInfo that represent base for User info.
 * 
 * @author Pavlo Degtyaryev
 */
public abstract class AbstractOAuth2UserInfo {

	/** Stored the User info attributes. */
	protected Map<String, Object> attributes;

	/**
	 * Instantiates a new {@link AbstractOAuth2UserInfo} using the {@link Map} of existing
	 * attributes.
	 *
	 * @param {@link Map} the attributes
	 */
	public AbstractOAuth2UserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Gets the {@link Map} with User attributes.
	 *
	 * @return the {@link Map}
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	/**
	 * Gets the Users OAuth Id.
	 *
	 * @return the {@link String} id
	 */
	public abstract String getId();

	/**
	 * Gets the Users display name.
	 *
	 * @return the {@link String} name
	 */
	public abstract String getName();

	/**
	 * Gets the Users email.
	 *
	 * @return the {@link String} email
	 */
	public abstract String getEmail();

	/**
	 * Gets the Users image url.
	 *
	 * @return the {@link String} image url
	 */
	public abstract String getImageUrl();
}
