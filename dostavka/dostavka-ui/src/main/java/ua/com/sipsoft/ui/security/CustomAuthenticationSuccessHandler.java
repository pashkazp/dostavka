package ua.com.sipsoft.ui.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.service.security.jwt.JwtTokenProvider;
import ua.com.sipsoft.ui.common.CookieUtils;
import ua.com.sipsoft.util.AppProperties;

/**
 * The class AuthenticationSuccessHandler that controls the overall process in
 * case of successful OAuth authentication.
 * 
 * @author Pavlo Degtyaryev
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtTokenProvider jwtTokenProvider;

	private final AppProperties appProperties;

	/**
	 * On authentication success.
	 *
	 * @param {@link HttpServletRequest} the request
	 * @param {@link HttpServletResponse} the response
	 * @param {@link Authentication} the authentication
	 * @throws {@link IOException} Signals that an I/O exception has occurred.
	 * @throws {@link ServletException} the servlet exception
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.debug("IN onAuthenticationSuccess - authentication successfull");
		String targetUrl = determineTargetUrl(request, response, authentication);

		if (response.isCommitted()) {
			log.debug("IN onAuthenticationSuccess - Response has already been committed. Unable to redirect to "
					+ targetUrl);
			return;
		}

		String token = jwtTokenProvider.createToken(authentication);
		log.debug("IN onAuthenticationSuccess - Add cookie '" + appProperties.getAuth().getTokenCookieName()
				+ "' to  HttpServletResponse");
		CookieUtils.addCookie(response, appProperties.getAuth().getTokenCookieName(), token, 84000);

		String redirectionUrl = UriComponentsBuilder.fromUriString("/")
				.build().toUriString();

		clearAuthenticationAttributes(request, response);
		getRedirectStrategy().sendRedirect(request, response, redirectionUrl);
	}

	/**
	 * Clear authentication attributes.
	 *
	 * @param request  the request
	 * @param response the response
	 */
	protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
		log.debug("IN clearAuthenticationAttributes - Clear authentication attributes");
		super.clearAuthenticationAttributes(request);
	}

}
