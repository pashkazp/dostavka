package ua.com.sipsoft.util.security;

/**
 * The Interface AgreedPasswordCheck to validate entered password.
 * 
 * @author Pavlo Degtyaryev
 */
public interface AgreedPasswordCheck {

	/** The password regexp. */
	// String passwordRegexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,50})|(^$)";
	String passwordRegexp = "((?=.*\\d)(?!.*\\s)(?=.*[a-z])(?=.*[A-Z]).{8,50})";

	/**
	 * Validity of the password.
	 *
	 * @param password the password
	 * @return true, if successful
	 */
	static boolean adreedPasswordCheck(String password) {
		return (password != null) && (password.matches(passwordRegexp));
	}

}
