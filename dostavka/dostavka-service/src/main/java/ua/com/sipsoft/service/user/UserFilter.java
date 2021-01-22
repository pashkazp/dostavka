package ua.com.sipsoft.service.user;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.defaultString;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.util.EntityFilter;
import ua.com.sipsoft.util.security.Role;

/**
 * The Interface UserFilter.
 * 
 * @author Pavlo Degtyaryev
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserFilter implements EntityFilter<User> {

	/** The id. */
	@Builder.Default
	private Long id = null;

	/** The User display name. */
	@Builder.Default
	private String name = null;

//	/** The first name. */
//	@Builder.Default
//	private String firstName = null;
//
//	/** The last name. */
//	@Builder.Default
//	private String lastName = null;
//
//	/** The patronymic. */
//	@Builder.Default
//	private String patronymic = null;

	/** The email. */
	@Builder.Default
	private String email = null;

	/** The roles. */
	@Builder.Default
	private Collection<Role> roles = null;

	@Override
	public String toString() {
		return String.format("UserFilter [id=%s, name=\"%s\", email=\"%s\", roles=%s]", id, name, email, roles);
	}

	/**
	 * Checks if is pass.
	 *
	 * @param entity the entity
	 * @return true, if is pass
	 */
	@Override
	public boolean isPass(User entity) {
		if (entity == null) {
			return false;
		}
		if (id != null && !id.equals(entity.getId())) {
			return false;
		}
		if (!containsIgnoreCase(entity.getName(), defaultString(name))) {
			return false;
		}
		if (!containsIgnoreCase(entity.getEmail(), defaultString(email))) {
			return false;
		}
		if (roles != null && !CollectionUtils.containsAny(roles, entity.getRoles())) {
			return false;
		}
		return true;
	}

}