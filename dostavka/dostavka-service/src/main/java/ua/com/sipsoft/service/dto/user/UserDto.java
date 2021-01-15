package ua.com.sipsoft.service.dto.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.sipsoft.util.security.Role;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements Serializable {

	private static final long serialVersionUID = 1848596179922865880L;

	private Long id;

	private Long version = 0L;

	@Email
	@NotEmpty
	private String email;

	private String password;

	@NotEmpty
	private String name;

	private Boolean enabled;

	private Boolean verified;

	private Set<Role> roles = new HashSet<>();

	public void addRoles(Iterable<Role> roles) {
		for (Role role : roles) {
			this.roles.add(role);
		}
	}

	public void addRoles(Role... roles) {
		for (Role role : roles) {
			this.roles.add(role);
		}
	}

	private int getPasswordLength() {
		return password == null ? 0 : password.length();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserDto [id=").append(id).append(", version=").append(version).append(", name=").append(name)
				.append(", email=").append(email).append(", password=").append("*".repeat(getPasswordLength()))
				.append(", enabled=").append(enabled).append(", verified=").append(verified).append(", roles=")
				.append(roles).append("]");
		return builder.toString();
	}
}
