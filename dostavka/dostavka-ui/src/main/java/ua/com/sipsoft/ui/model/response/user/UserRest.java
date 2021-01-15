package ua.com.sipsoft.ui.model.response.user;

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
public class UserRest implements Serializable {

	private static final long serialVersionUID = 1848596179922865880L;

	private Long id;

	@Email
	@NotEmpty
	private String email;

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
}
