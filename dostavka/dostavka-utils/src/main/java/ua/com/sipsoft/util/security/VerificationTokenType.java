package ua.com.sipsoft.util.security;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;

/**
 * The type of token used to request a change in a forgotten password or to
 * confirm e-mail when registering a new user.
 * 
 * @author Pavlo Degtyaryev
 */

public enum VerificationTokenType {

	// Warning! Sequence matters for safety. Higher suits have lower numbers.
	REGNEWUSER("RNU"),

	FORGOTPASS("FGP");

	/**
	 * Gets the short name.
	 *
	 * @return the short name
	 */
	@Getter
	private final String shortName;

	/**
	 * Instantiates a new verification token type.
	 *
	 * @param shortName the short name
	 */
	private VerificationTokenType(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * Return VerificationTokenType by it name
	 *
	 * @param shortName the short name
	 * @return the verification token type
	 */
	public static VerificationTokenType fromShortName(String shortName) {
		for (VerificationTokenType type : VerificationTokenType.values()) {
			if (type.getShortName().equals(StringUtils.defaultString(shortName.toUpperCase()))) {
				return type;
			}
		}
		throw new UnsupportedOperationException(
				"The VerificationTokenType short name '" + shortName + "' is not supported!");
	}

}