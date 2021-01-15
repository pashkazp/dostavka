package ua.com.sipsoft.util.paging;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class that represents Search criteria for request.
 *
 * @author Pavlo Degtyaryev
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Search implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6120313698131430056L;

	/** The value. */
	private String value;

	/** The regexp. */
	private String regexp;
}