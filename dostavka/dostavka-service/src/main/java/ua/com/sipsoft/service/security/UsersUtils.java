package ua.com.sipsoft.service.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.util.security.Role;

public final class UsersUtils {

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public static User getCurrentUser() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context.getAuthentication() == null || !context.getAuthentication().isAuthenticated()) {
			return null;
		}
		Object principal = context.getAuthentication().getPrincipal();
		if (principal instanceof UserPrincipal) {
			UserPrincipal userDetails = (UserPrincipal) context.getAuthentication().getPrincipal();
			return userDetails.getUser();
		}
		// Anonymous or no authentication.
		return null;
	}

	/**
	 * Gets the user roles.
	 *
	 * @return the user roles
	 */
	public static Collection<Role> getUserRoles() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context.getAuthentication() == null || !context.getAuthentication().isAuthenticated()) {
			return Collections.emptySet();
		}
		Object principal = context.getAuthentication().getPrincipal();
		if (principal instanceof UserPrincipal) {
			UserPrincipal userDetails = (UserPrincipal) context.getAuthentication().getPrincipal();
			return userDetails.getRoles();
		}
		// Anonymous or no authentication.
		return Collections.emptySet();
	}

	/**
	 * Gets the highest role. Where Admin - highest and User - lowest. If Role is
	 * absent return User
	 *
	 * @return the highest {@link Role}
	 */
	public static Role getHighestUserRole() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context.getAuthentication() == null || !context.getAuthentication().isAuthenticated()) {
			return null;
		}
		Object principal = context.getAuthentication().getPrincipal();
		if (principal instanceof UserPrincipal) {
			UserPrincipal userDetails = (UserPrincipal) context.getAuthentication().getPrincipal();
			return userDetails.getHighestRole();
		}
		// Anonymous or no authentication.
		return Role.ROLE_USER;
	}

	/**
	 * Gets the user Email as Usename of the currently signed in user.
	 *
	 * @return the user name of the current user or <code>null</code> if the user
	 *         has not signed in
	 */
	public static String getUsername() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context.getAuthentication() == null || !context.getAuthentication().isAuthenticated()) {
			return null;
		}
		Object principal = context.getAuthentication().getPrincipal();
		if (principal instanceof UserPrincipal) {
			UserPrincipal userDetails = (UserPrincipal) context.getAuthentication().getPrincipal();
			return userDetails.getUsername();
		}
		// Anonymous or no authentication.
		return null;
	}

	public static String getEmail() {
		return getUsername();
	}

	public static UserPrincipal getUserPrincipal() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context.getAuthentication() == null || !context.getAuthentication().isAuthenticated()) {
			return null;
		}
		Object principal = context.getAuthentication().getPrincipal();
		if (principal instanceof UserPrincipal) {
			return (UserPrincipal) context.getAuthentication().getPrincipal();
		}
		// Anonymous or no authentication.
		return null;
	}

}
