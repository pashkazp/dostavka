package ua.com.sipsoft.ui.common;

import java.util.Base64;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.SerializationUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * The project uses some utility classes to perform various tasks
 * 
 * @author Pavlo Degtyaryev
 */
@Slf4j
public class CookieUtils {

	private static String cookieRootPath = "/dostavka";

	/**
	 * Gets the {@link Optional}<{@link Cookie}> cookie from
	 * {@link HttpServletRequest} request by cookie name.
	 *
	 * @param HttpServletRequest the request
	 * @param String             the cookie name
	 * @return Optional the cookie
	 */
	public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {

		log.debug("IN getCookie - perform get Cookie from HttpServletRequest by name: '{}'", name);
		Cookie[] cookies = request.getCookies();

		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					log.debug("IN getCookie - cookie name '{}' is found. Return: '{}'.", name, "***");
					return Optional.of(cookie);
				}
			}
		}

		log.debug("IN getCookie - cookie is not found. Return empty Optional.");
		return Optional.empty();
	}

	/**
	 * Adds the {@link Cookie} to the {@link HttpServletResponse} response.
	 *
	 * @param HttpServletResponse the response
	 * @param String              the cookie name
	 * @param value               the cookie value
	 * @param maxAge              the max cookie age
	 */
	public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {

		log.debug("IN addCookie - add to HttpServletResponse the Cookie with name '{}' value '{}' and maxAge '{}' .",
				name, value,
				maxAge);
		Cookie cookie = new Cookie(name, value);
		cookie.setPath(cookieRootPath + "/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}

	/**
	 * Delete the {@link Cookie} named cookie by set it max age to zero.
	 *
	 * @param HttpServletRequest  the request
	 * @param HttpServletResponse the response
	 * @param String              the Cookie name
	 */
	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		log.debug(
				"IN deleteCookie - search Cookie with name '{}' in HttpServletRequest and delete it if found in HttpServletResponse",
				name);
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					cookie.setValue("");
					cookie.setPath(cookieRootPath + "/");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
	}

	/**
	 * Serialize any Object to Base64 string.
	 *
	 * @param Object the object
	 * @return String the Base64 encoded string
	 */
	public static String serialize(Object object) {
		log.debug("IN serialize - serialize by Base64 encoder the Object '{}'", object.toString());
		return Base64.getUrlEncoder()
				.encodeToString(SerializationUtils.serialize(object));
	}

	/**
	 * Deserialize Base64 encoded Class in cookie to given class.
	 *
	 * @param Class  <T> the generic type
	 * @param Cookie the cookie
	 * @param Class  the cls
	 * @return lass the t
	 */
	public static <T> T deserialize(Cookie cookie, Class<T> cls) {
		log.debug("IN deserialize - deserialize cookie '{}'", cookie);
		return cls.cast(SerializationUtils.deserialize(
				Base64.getUrlDecoder().decode(cookie.getValue())));
	}

}
