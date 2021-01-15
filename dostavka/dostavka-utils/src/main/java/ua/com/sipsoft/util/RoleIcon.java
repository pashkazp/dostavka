package ua.com.sipsoft.util;

/**
 * icons that represents standard icons of user roles.
 *
 * @author Pavlo Degtyaryev
 */
public enum RoleIcon {

	USER(UIIcon.USERROLE_USER),
	CLIENT(UIIcon.USERROLE_CLIENT),
	COURIER(UIIcon.USERROLE_COURIER),
	MANAGER(UIIcon.USERROLE_MANAGER),
	PRODUCTOPER(UIIcon.USERROLE_PRODUCTOPER),
	DISPATCHER(UIIcon.USERROLE_DISPATCHER),
	ADMIN(UIIcon.USERROLE_ADMIN);

	/** The icon. */
	private final UIIcon icon;

	/**
	 * Instantiates a new role icon.
	 *
	 * @param userroleUser the icon
	 */
	private RoleIcon(UIIcon userroleUser) {
		this.icon = userroleUser;
	}

	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public UIIcon getIcon() {
		return icon;
	}

	/**
	 * Gets the class CSS.
	 *
	 * @return the class CSS
	 */
	public String getClassCSS() {
		return icon.getClassCSS();
	}

}