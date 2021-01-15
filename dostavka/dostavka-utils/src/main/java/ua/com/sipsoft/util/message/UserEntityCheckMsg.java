package ua.com.sipsoft.util.message;

/**
 *
 * A class that groups string constants that describe a check user record
 * 
 * @author Pavlo Degtyaryev
 */
public class UserEntityCheckMsg {
	public static final String CONFPASS = "entity.user.check.confirmpassword";
	public static final String CONFPASS_EXPECT = "entity.user.check.confirmpassword.expect";
	public static final String PASS_CHR = "entity.user.check.password.characters";
	public static final String PASS_EQUAL = "entity.user.check.password.equal";
	public static final String PASS_EXPECT = "entity.user.check.password.expect";
	public static final String EMAIL_CHR = "entity.user.check.email.characters";
	public static final String EMAIL_EXPECT = "entity.user.check.email.expect";
	public static final String EMAIL_NOTFOUND = "entity.user.check.email.notfound";
	public static final String EMAIL_TAKE_OTHER = "entity.user.check.email.takeother";
	public static final String ROLE_QTY = "entity.user.check.role.qty";
	public static final String NAME_CHR = "entity.user.check.name.characters";

	public static final String SMALL_NAME = "entity.user.check.name.short";
//	public static final String LONG_FIRSTNAME = "entity.user.check.firstname.long";
//	public static final String LONG_SECONDNAME = "entity.user.check.secondname.long";
//	public static final String LONG_PATRONYMIC = "entity.user.check.patronymic.long";

	/**
	 * Instantiates a new user entity check msg.
	 */
	private UserEntityCheckMsg() {
	}
}
