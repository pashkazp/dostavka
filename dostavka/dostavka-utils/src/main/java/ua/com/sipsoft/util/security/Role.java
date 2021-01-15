package ua.com.sipsoft.util.security;

import ua.com.sipsoft.util.RoleIcon;
import ua.com.sipsoft.util.message.RolesMsg;

/**
 * Simple JavaBean domain object that represents {@link User}'s role.
 *
 * @author Pavlo Degtyaryev
 */

public enum Role /* implements GrantedAuthority */ {

	/** The role admin. */
	// Warning! Sequence matters for safety. Higher suits have lower numbers.
	ROLE_ADMIN(RoleName.ROLE_ADMIN, RoleIcon.ADMIN, RolesMsg.ROLE_ADMIN),

	/** The role dispatcher. */
	ROLE_DISPATCHER(RoleName.ROLE_DISPATCHER, RoleIcon.DISPATCHER, RolesMsg.ROLE_DISPATCHER),

	/** The role manager. */
	ROLE_MANAGER(RoleName.ROLE_MANAGER, RoleIcon.MANAGER, RolesMsg.ROLE_MANAGER),

	/** The role productoper. */
	ROLE_PRODUCTOPER(RoleName.ROLE_PRODUCTOPER, RoleIcon.PRODUCTOPER, RolesMsg.ROLE_PRODUCTOPER),

	/** The role courier. */
	ROLE_COURIER(RoleName.ROLE_COURIER, RoleIcon.COURIER, RolesMsg.ROLE_COURIER),

	/** The role client. */
	ROLE_CLIENT(RoleName.ROLE_CLIENT, RoleIcon.CLIENT, RolesMsg.ROLE_CLIENT),

	/** The role user. */
	ROLE_USER(RoleName.ROLE_USER, RoleIcon.USER, RolesMsg.ROLE_USER);

	/** The Role Name is the static final String from {@link RoleName} class. */
	private final String roleName;

	/** The icon. */
	private final RoleIcon icon;

	/** The message bundle role name from {@link RolesMsg} class. */
	private final String roleNameMessage;

	/**
	 * Instantiates a new role.
	 *
	 * @param roleName        the roleName
	 * @param icon            the icon
	 * @param roleNameMessage the role name
	 */
	private Role(String roleName, RoleIcon icon, String roleNameMessage) {
		this.roleName = roleName;
		this.icon = icon;
		this.roleNameMessage = roleNameMessage;
	}

	/**
	 * Gets the authority.
	 *
	 * @return the authority
	 */
//	@Override
//	public String getAuthority() {
//		return roleName;
//	}
//
	/**
	 * Gets the roleName.
	 *
	 * @return the roleName
	 */
	public final String getRoleName() {
		return roleName;
	}

	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public RoleIcon getIcon() {
		return icon;
	}

	/**
	 * Gets the role name.
	 *
	 * @return the role name
	 */
	public String getRoleNameMessage() {
		return roleNameMessage;
	}

}