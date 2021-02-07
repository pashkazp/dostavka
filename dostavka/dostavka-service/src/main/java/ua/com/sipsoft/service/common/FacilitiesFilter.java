package ua.com.sipsoft.service.common;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.defaultString;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.sipsoft.dao.common.Facility;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.util.EntityFilter;
import ua.com.sipsoft.util.security.Role;

/**
 * The Class FacilitiesFilter.
 *
 * @author Pavlo Degtyaryev
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class FacilitiesFilter implements EntityFilter<Facility> {

	/** The name. */
	@Builder.Default
	private String name = null;

	/** The users. */
	@Builder.Default
	private Set<User> users = null;

	/** The users. */
	@Builder.Default
	private User caller = null;

	@Override
	public String toString() {
		return String.format("FacilitiesFilter [name=\"%s\", users=%s]", name, users);
	}

	/**
	 * Checks if is pass.
	 *
	 * @param entity the entity
	 * @return true, if is pass
	 */
	@Override
	public boolean isPass(Facility entity) {
		if (entity == null) {
			return false;
		}
		if (!containsIgnoreCase(entity.getName(), defaultString(name))) {
			return false;
		}
		if (users != null && !CollectionUtils.containsAny(users, entity.getUsers())) {
			return false;
		}
		if (caller != null) {
			if (caller.getHighesRole().ordinal() == Role.ROLE_CLIENT.ordinal()) {
				if (!CollectionUtils.containsAny(entity.getUsers(), caller)) {
					return false;
				}
			}
		}
		return true;
	}

	public void addUsers(User... users) {
		if (users.length > 0 && this.users == null) {
			this.users = new HashSet<User>();
		}
		for (User user : users) {
			this.users.add(user);
		}

	}

}