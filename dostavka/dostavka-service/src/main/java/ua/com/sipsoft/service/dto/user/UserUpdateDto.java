package ua.com.sipsoft.service.dto.user;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserUpdateDto extends BaseUserInfoDto implements Serializable {

	private static final long serialVersionUID = 1848596179922865880L;

	private Long id;

	private Boolean enabled;

	private Boolean verified;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserRegistrationDto [name=").append(getName()).append(", email=").append(getEmail())
				.append(", password=").append("*".repeat(getPasswordLength()))
				.append(", roles=").append(getRoles()).append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(enabled, id, verified);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof UserUpdateDto)) {
			return false;
		}
		UserUpdateDto other = (UserUpdateDto) obj;
		return Objects.equals(enabled, other.enabled) && Objects.equals(id, other.id)
				&& Objects.equals(verified, other.verified);
	}
}
