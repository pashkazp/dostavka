package ua.com.sipsoft.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * Class that represents the some properties from
 * application.properties(application.yml) file as simple classes.
 *
 * @author Pavlo Degtyaryev
 */
@ConfigurationProperties(prefix = "app")
public class AppProperties {
	/** The auth. */
	private final Auth auth = new Auth();

//	/** The oauth 2. */
//	private final OAuth2 oauth2 = new OAuth2();

	/** The app. */
	private final App app = new App();

	/**
	 * The Class Auth.
	 */
	public static class Auth {

		/** The token Issurer. */
		@Getter
		@Setter
		private String tokenIssurer;

		/** The token secret. */
		@Getter
		@Setter
		// TODO Must be changed before commissioning
		private String tokenSecret;

		/** The token expiration msec. */
		@Getter
		@Setter
		private long tokenExpirationMsec;

		@Getter
		@Setter
		private String tokenCookieName;

		@Getter
		@Setter
		private String tokenPrefix;

	}

//	/**
//	 * The Class OAuth2.
//	 */
//	public static final class OAuth2 {
//
//		/** The authorized redirect uris. */
//		private List<String> authorizedRedirectUris = new ArrayList<>();
//
//		/**
//		 * Gets the authorized redirect uris.
//		 *
//		 * @return the authorized redirect uris
//		 */
//		public List<String> getAuthorizedRedirectUris() {
//			return authorizedRedirectUris;
//		}
//	}

	/**
	 * The Class App.
	 */
	public static final class App {

		@Getter
		@Setter
		private String domain;
	}

	/**
	 * Gets the auth.
	 *
	 * @return the auth
	 */
	public Auth getAuth() {
		return auth;
	}

	/**
	 * Gets the oauth 2.
	 *
	 * @return the oauth 2
	 */
//	public OAuth2 getOauth2() {
//		return oauth2;
//	}

	/**
	 * Gets the app.
	 *
	 * @return the app
	 */
	public App getApp() {
		return app;
	}
}
