package ua.com.sipsoft.util.security;

import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class SecurityUtils.
 *
 * @author Pavlo Degtyaryev
 */
@Slf4j
public final class SecurityUtils {

	/** The Constant walker. */
	static final StackWalker stackWalker = StackWalker.getInstance(RETAIN_CLASS_REFERENCE);

	/**
	 * Instantiates a new security utils.
	 */
	private SecurityUtils() {
		// Util methods only
	}

	/**
	 * Checks if the user is logged in.
	 *
	 * @return true if the user is logged in. False otherwise.
	 */
	public static boolean isUserLoggedIn() {
		return isUserLoggedIn(SecurityContextHolder.getContext().getAuthentication());
	}

	/**
	 * Checks if is user logged in.
	 *
	 * @param authentication the authentication
	 * @return true, if is user logged in
	 */
	private static boolean isUserLoggedIn(Authentication authentication) {
		return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
	}

	/**
	 * Check annotation {@link AllowedFor} for Class. If annotations exist and have
	 * any ROLES, then return true for the user who has one of these roles
	 *
	 * @param securedClass the secured class
	 * @return true, if is access granted
	 */
	public static boolean isAccessGranted(Class<?> securedClass) {
		log.info("Check access for Class '{}'", securedClass);
		if (securedClass == null) {
			return true;
		}
		AllowedFor allowedFor = AnnotationUtils.findAnnotation(securedClass, AllowedFor.class);
		if (allowedFor == null) {
			return true; //
		}

		// lookup needed role in user roles
		List<String> allowedRoles = Arrays.asList(allowedFor.value());
		log.debug("Found @AllowedFor annotation '{}'", allowedFor.toString());
		Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
		log.debug("Check access for User '{}'", userAuthentication.getName());
		return userAuthentication.getAuthorities().stream() //
				.map(GrantedAuthority::getAuthority)
				.peek(a -> log.debug("Found authorityes '{}'", a))
				.anyMatch(allowedRoles::contains);
	}

	/**
	 * Check annotation {@link AllowedFor} for method. If annotations exist and have
	 * any ROLES, then return true for the user who has one of these roles
	 *
	 * @param method the method
	 * @return true, if is access granted
	 */
	public static boolean isAccessGranted(Method method) {
		log.info("Check access for Method '{}'", method);
		if (method == null) {
			return true;
		}
		AllowedFor allowedFor = AnnotationUtils.findAnnotation(method, AllowedFor.class);
		if (allowedFor == null) {
			return true;
		}
		List<String> allowedRoles = Arrays.asList(allowedFor.value());
		log.debug("Found @AllowedFor annotation '{}'", allowedFor.toString());
		Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
		log.debug("Check access for User '{}'", userAuthentication.getName());
		return userAuthentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.peek(a -> log.debug("Found authorityes '{}'", a))
				.anyMatch(allowedRoles::contains);
	}

	/**
	 * Find enclosing Class and checks that is access granted.
	 *
	 * @return true, if is access granted
	 */
	public static boolean isClassAccessGranted() {
		Class<?> callerClass = stackWalker.getCallerClass();
		return isAccessGranted(callerClass);
	}

	/**
	 * Find enclosing method and checks that the found method is access granted.
	 *
	 * @return true, if is access granted
	 */
	public static boolean isMethodAccessGranted() {
		StackTraceElement[] stackTrace = Thread.currentThread()
				.getStackTrace();
		StackTraceElement e = stackTrace[2];
		log.info("Check access for current method '{}'", e.getMethodName());
		try {
			return isAccessGranted(Class.forName(e.getClassName())
					.getDeclaredMethod(e.getMethodName()));
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e1) {
			return true;
		}
	}

	/**
	 * Gets the user authorities.
	 *
	 * @return the user authorities
	 */
	public static Collection<String> getUserAuthorities() {
		Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
		log.info("Gets authorities for user '{}'", userAuthentication.getName());
		return userAuthentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

	}
}