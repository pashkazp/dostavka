package ua.com.sipsoft.service.dto.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.sipsoft.util.security.Role;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseUserInfoDto implements Serializable {

	private static final long serialVersionUID = 8630189091353382968L;

	@Email
	@NotEmpty
	private String email;

	@NotEmpty
	private String password;

	@NotEmpty
	private String name;

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

	int getPasswordLength() {
		return password == null ? 0 : password.length();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserRegistrationDto [name=").append(name).append(", email=").append(email)
				.append(", password=").append("*".repeat(getPasswordLength()))
				.append(", roles=").append(roles).append("]");
		return builder.toString();
	}
}
