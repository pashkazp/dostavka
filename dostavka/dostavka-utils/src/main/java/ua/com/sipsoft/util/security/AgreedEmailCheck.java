package ua.com.sipsoft.util.security;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * The Interface AgreedEmailCheck to validate entered Email .
 * 
 * @author Pavlo Degtyaryev
 */
public interface AgreedEmailCheck {

	/**
	 * Validity of the email address
	 *
	 * @param email the email
	 * @return true, if successful
	 */
	static boolean agreedEmailCheck(String email) {
		return (email != null) && (email.length() <= 100) && EmailValidator.getInstance().isValid(email);
	}

}