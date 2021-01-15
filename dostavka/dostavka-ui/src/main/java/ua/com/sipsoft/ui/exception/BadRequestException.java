package ua.com.sipsoft.ui.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The Class BadRequestException used throughout the application for Bad Request
 * exceprion.
 * 
 * @author Pavlo Degtyeryev
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2053793053809932653L;

	/**
	 * Instantiates a new bad request exception.
	 *
	 * @param String the message
	 */
	public BadRequestException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new bad request exception.
	 *
	 * @param String    the message
	 * @param Throwable the cause
	 */
	public BadRequestException(String message, Throwable cause) {
		super(message, cause);
	}

}
