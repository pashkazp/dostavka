package ua.com.sipsoft.ui.model.request.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.AssertTrue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.sipsoft.service.dto.constraint.FieldMatch;
import ua.com.sipsoft.util.security.Role;

@FieldMatch.List({
		@FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match"),
})
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegistrationRequest implements Serializable {

	private static final long serialVersionUID = 1848596179922865880L;

	private String email;

	private String password;

	private String confirmPassword;

	private String name;

	private Set<Role> roles = new HashSet<>();

	@AssertTrue
	private Boolean terms = false;

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

	private int getCPasswordLength() {
		return confirmPassword == null ? 0 : confirmPassword.length();
	}

	private int getPasswordLength() {
		return password == null ? 0 : password.length();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserRegistrationRequest [name=").append(name).append(", email=").append(email)
				.append(", password=").append("*".repeat(getPasswordLength())).append(", confirmPassword=")
				.append("*".repeat(getCPasswordLength()))
				.append(", roles=").append(roles).append(", terms=").append(terms).append("]");
		return builder.toString();
	}
}
