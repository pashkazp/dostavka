package ua.com.sipsoft.util.security;

/**
 * The Interface AgreedPasswordCheck to validate entered username.
 * 
 * @author Pavlo Degtyaryev
 */
public interface AgreedUsernameCheck {

	/** The username regexp. */
	String usernameRegexp = "^.{3,32}$";
//	String usernameRegexp = "^([A-Za-z0-9]){3,32}$";

	/**
	 * Validity of the username.
	 *
	 * @param name the name
	 * @return true, if successful
	 */
	static boolean agreedUsernameCheck(String name) {
		return (name != null) && (name.matches(usernameRegexp));
	}
}
