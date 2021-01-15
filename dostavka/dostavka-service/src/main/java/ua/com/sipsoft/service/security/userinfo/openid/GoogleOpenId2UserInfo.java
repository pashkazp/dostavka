package ua.com.sipsoft.service.security.userinfo.openid;

import java.util.Map;

import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoogleOpenId2UserInfo extends OidcUserInfo {

	private static final long serialVersionUID = 5276120099165620944L;

	public GoogleOpenId2UserInfo(Map<String, Object> claims) {
		super(claims);
		log.debug("Instantiate Google Oidc user");
	}

	/**
	 * Gets the sub attribute as Oidc user Id.
	 *
	 * @return the {@link String} id
	 */
	public String getId() {
		return (String) getClaims().get("id");
	}

	/**
	 * Gets the name attribute as Oidc user Name.
	 *
	 * @return the {@link String} name
	 */
	public String getName() {
		return (String) getClaims().get("name");
	}

	/**
	 * Gets the Email attribute as Oidc user Email.
	 *
	 * @return the {@link String} email
	 */
	@Override
	public String getEmail() {
		return (String) getClaims().get("email");
	}

	/**
	 * Gets the picture attribute as Oidc user Image Url.
	 *
	 * @return the {@link String} image url
	 */
	public String getImageUrl() {
		return (String) getClaims().get("picture");
	}
}
