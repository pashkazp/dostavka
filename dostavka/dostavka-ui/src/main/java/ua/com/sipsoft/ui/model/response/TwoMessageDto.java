
package ua.com.sipsoft.ui.model.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Class TwoMessageDto that represent two strings response.
 * 
 * @author Pavlo Degtyaryev
 */
@Getter
@Setter
@NoArgsConstructor
public class TwoMessageDto implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2881755799207255407L;

	private String messageOne;
	private String messageTwo;

	/**
	 * Instantiates a new two messages response.
	 *
	 * @param messageOne the message one
	 * @param messageTwo the message two
	 */
	public TwoMessageDto(String messageOne, String messageTwo) {
		this.messageOne = messageOne;
		this.messageTwo = messageTwo;
	}

}
