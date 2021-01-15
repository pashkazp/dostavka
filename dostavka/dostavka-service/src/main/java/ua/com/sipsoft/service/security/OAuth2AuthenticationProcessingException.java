package ua.com.sipsoft.service.security;

import org.springframework.security.core.AuthenticationException;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class OAuth2AuthenticationProcessingException.
 * 
 * @author Pavlo Degtyaryev
 */
@Slf4j
public class OAuth2AuthenticationProcessingException extends AuthenticationException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4230062081286351762L;

	/**
	 * Instantiates a new OAuth2AuthenticationProcessingException.
	 *
	 * @param String    the msg
	 * @param Throwable the t
	 */
	public OAuth2AuthenticationProcessingException(String msg, Throwable t) {
		super(msg, t);
		log.debug("Instantiate OAuth2 Authentication Processing Exception");
	}

	/**
	 * Instantiates a new o auth 2 authentication processing exception.
	 *
	 * @param String the msg
	 */
	public OAuth2AuthenticationProcessingException(String msg) {
		super(msg);
		log.debug("Instantiate OAuth2 Authentication Processing Exception");
	}
}
