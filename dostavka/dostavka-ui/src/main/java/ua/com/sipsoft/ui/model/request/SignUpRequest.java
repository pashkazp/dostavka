package ua.com.sipsoft.ui.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class SignUpRequest a request to SignUp
 * 
 * @author Pavlo Degtyaryev
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUpRequest {

	/** The name. */
	@NotBlank
	private String name;

	/** The email. */
	@NotBlank
	@Email
	private String email;

	/** The password. */
	@NotBlank
	private String password;

	private int getPasswordLength() {
		return password == null ? 0 : password.length();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SignUpRequest [name=").append(name).append(", email=").append(email).append(", password=")
				.append("*".repeat(getPasswordLength())).append("]");
		return builder.toString();
	}
}
