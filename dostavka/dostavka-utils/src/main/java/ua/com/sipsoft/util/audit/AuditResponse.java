
package ua.com.sipsoft.util.audit;

import java.io.Serializable;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class represents response on New User or Edit User request
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AuditResponse implements Serializable {

	private static final long serialVersionUID = -487372227707050769L;

	/** the truth means that all checks have been completed successfully */
	private boolean valid;

	/** The map of messages where field name is key */
	private Multimap<String, String> messages = TreeMultimap.create();

	/**
	 * Adds the error message.
	 *
	 * @param key     the field name
	 * @param message the error message
	 */
	public void addMessage(String key, String message) {
		messages.put(key, message);
	}

	public boolean isEmpty() {
		return messages.isEmpty();
	}

	public boolean isInvalid() {
		return !isValid();
	}
}
