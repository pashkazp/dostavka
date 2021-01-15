package ua.com.sipsoft.service.security;

import org.springframework.security.core.AuthenticationException;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class OpenId2AuthenticationProcessingException.
 * 
 * @author Pavlo Degtyaryev
 */
@Slf4j
public class OpenId2AuthenticationProcessingException extends AuthenticationException {

	private static final long serialVersionUID = 4295729808042374906L;

	/**
	 * Instantiates a new OAuth2AuthenticationProcessingException.
	 *
	 * @param String    the msg
	 * @param Throwable the t
	 */
	public OpenId2AuthenticationProcessingException(String msg, Throwable t) {
		super(msg, t);
		log.debug("Instantiate OpenId Authentication Processing Exception");
	}

	/**
	 * Instantiates a new o auth 2 authentication processing exception.
	 *
	 * @param String the msg
	 */
	public OpenId2AuthenticationProcessingException(String msg) {
		super(msg);
		log.debug("Instantiate OpenId Authentication Processing Exception");
	}
}
