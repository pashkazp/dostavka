package ua.com.sipsoft.ui.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class ApiResponse represent an API_V1 response unit.
 *
 * @author Pavlo Degtyaryev
 */
@Getter
@Setter
@NoArgsConstructor
public class ApiResponse {

	/** The success. */
	private boolean success;

	/** The message. */
	private String message;

	/**
	 * Instantiates a new api response.
	 *
	 * @param the success of {@link boolean} type
	 * @param the message of {@link String} type
	 */
	public ApiResponse(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

}
