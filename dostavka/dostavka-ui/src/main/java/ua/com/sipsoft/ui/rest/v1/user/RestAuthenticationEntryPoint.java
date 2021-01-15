package ua.com.sipsoft.ui.rest.v1.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is invoked when a user tries to access a protected resource
 * without authentication. In this case, it return a 401 Unauthorized response
 * 
 * @author Pavlo Degtyaryev
 */
@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	/**
	 * Commence.
	 *
	 * @param HttpServletRequest      the http servlet request
	 * @param HttpServletResponse     the http servlet response
	 * @param AuthenticationException the e
	 * @throws IOException      Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@Override
	public void commence(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			AuthenticationException e) throws IOException, ServletException {
		log.error("Responding with unauthorized request. Message - {}", e.getMessage());
		httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
				e.getLocalizedMessage());
	}
}
