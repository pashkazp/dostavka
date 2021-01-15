package ua.com.sipsoft.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * Class that represents main URL's of Logistic Application.
 *
 * @author Pavlo Degtyaryev
 */
@EnableConfigurationProperties(AppProperties.class)
@Configuration
@Slf4j
public class AppURL {

	public static final String API_V1 = "/api/v1";
	public static final String AUTH = "/auth";
	public static final String FACILITIES = "/facilities";
	public static final String FACILITIESADDR = "/facility-addr";
	public static final String HOME = "/";
	public static final String ERROR = "/error";
	public static final String LOGIN = "/login";
	public static final String OAUTH2 = "/oauth2";
	public static final String PAGES = "/pages";
	public static final String REQUEST = "/request";
	public static final String SIGNUP = "/signup";
	public static final String USERS = "/users";

	public static final String OAUTH2_AUTORIZE = OAUTH2 + "/authorize"; // /oauth2/authorize
	public static final String OAUTH2_CALLBACK = OAUTH2 + "/callback"; // /oauth2/callback

	public static final String LOGIN_REGISTRATION = LOGIN + "/registration"; // /login/registration
	public static final String LOGIN_FORGOT = LOGIN + "/forgot"; // /login/forgot
	public static final String LOGIN_PROCESSING_URL = "loginprocessing";
	public static final String LOGIN_FAILURE_URL = "loginfail";

	public static final String API_V1_AUTH = API_V1 + AUTH;
	public static final String API_V1_AUTH_LOGIN = API_V1 + AUTH + LOGIN;
	public static final String API_V1_AUTH_SIGNUP = API_V1 + AUTH + SIGNUP;
	public static final String API_V1_FACILITIES = API_V1 + FACILITIES;
	public static final String API_V1_FACILITIES_PAGES = API_V1 + FACILITIES + PAGES;
	public static final String API_V1_FACILITIESADDR = API_V1 + FACILITIESADDR;
	public static final String API_V1_FACILITIESADDR_PAGES = API_V1 + FACILITIESADDR + PAGES;
	public static final String API_V1_USERS = API_V1 + USERS;
	public static final String API_V1_USERS_PAGES = API_V1 + USERS + PAGES;
	public static final String API_V1_USERS_ME = API_V1 + USERS + "/me";

	public static final String LOGOUT_SUCCESS_URL = "loginsuccess";
	public static final String LOGOUT_URL = "logout";

	public static final String ACCESS_DENIED_URL = ERROR + "/403";
	public static final String REGISTRATION_CONFIRM = "registration/confirm";

	public static final String REQUESTS_ALL = "requests";

	public static final String USER_ENV = API_V1 + "/env";

	public static final String USERS_PAGES = USERS + PAGES;

	public static final String ADMINS_ALL = "alladmins";
	public static final String DISPATCHERS_ALL = "alldispatchers";
	public static final String PRODUCTOPERS_ALL = "allproductopers";
	public static final String MANAGERS_ALL = "allmanagers";
	public static final String COURIERS_ALL = "allcouriers";
	public static final String CLIENTS_ALL = "allclients";
	public static final String USERS_ALL = "allusers";

	public static final String FACILITIES_PAGES = FACILITIES + PAGES;

	public static final String FACILITIESADDR_PAGES = FACILITIESADDR + PAGES;

	public static final String DRAFT_SHEETS = "draftsheet";
	public static final String ISSUED = "issued";
	public static final String ARCHIVE = "archive";

	public static String APP_DOMAIN;

	/**
	 * Instantiates a new app URL.
	 */
	public AppURL(@Autowired AppProperties appProperties) {
		// domain: http://localhost:8080
		APP_DOMAIN = appProperties.getApp().getDomain();
	}

	/**
	 * App url.
	 *
	 * @return the app URL
	 */
	@Bean
	public AppURL appUrl() {
		return this;
	}

}