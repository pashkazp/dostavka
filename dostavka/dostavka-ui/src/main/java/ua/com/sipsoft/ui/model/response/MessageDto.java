package ua.com.sipsoft.ui.model.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class OneMessageDto that represent one strings response.
 * 
 * @author Pavlo Degtyaryev
 */

@Getter
@Setter
@NoArgsConstructor
public class MessageDto implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2881755799207255407L;

	/** The message. */
	private String message;

	/**
	 * Instantiates a new One message dto.
	 *
	 * @param String the message
	 */
	public MessageDto(String message) {
		this.message = message;
	}

}
