package ua.com.sipsoft.ui.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class AuthResponse represent response unit.
 *
 * @author Pavlo Degtyaryev
 */
@Getter
@Setter
@NoArgsConstructor
public class AuthResponse {

	/** The access token. */
	private String accessToken;

	/** The token type. */
	private String tokenType = "Bearer";

	/**
	 * Instantiates a new auth response.
	 *
	 * @param accessToken the access token
	 */
	public AuthResponse(String accessToken) {
		this.accessToken = accessToken;
	}
}
