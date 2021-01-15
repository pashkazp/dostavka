package ua.com.sipsoft.ui.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.util.AppURL;

/**
 * The Class CustomAccessDeniedHandler that redirect unathorized request to
 * Access Denied Page.
 * 
 * @author Pavlo Degtyaryev
 * @version 1.0
 */
@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	/**
	 * Handle unauthorized request and redirect it to Access Denied Page.
	 *
	 * @param request               the HttpServletRequest
	 * @param response              the HttpServletResponse
	 * @param accessDeniedException the access denied exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			log.warn("IN handle - User: " + auth.getName()
					+ " attempted to access the protected URL: "
					+ request.getRequestURI());
		} else {
			log.warn("IN handle - Unautorized User attempted to access the protected URL: "
					+ request.getRequestURI());
		}

		response.sendRedirect(request.getContextPath() + AppURL.ACCESS_DENIED_URL);
	}
}
