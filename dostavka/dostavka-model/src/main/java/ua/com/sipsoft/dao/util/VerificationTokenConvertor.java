package ua.com.sipsoft.dao.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import ua.com.sipsoft.util.security.VerificationTokenType;

/**
 * The Class VerificationTokenConvertor to convert database value to token enum
 * and enum to column value
 * 
 * @author Pavlo Degtyaryev
 */
@Converter
public class VerificationTokenConvertor implements AttributeConverter<VerificationTokenType, String> {

	/**
	 * Convert to database column.
	 *
	 * @param token the token
	 * @return the string
	 */
	@Override
	public String convertToDatabaseColumn(VerificationTokenType token) {
		if (token == null) {
			return null;
		}
		return token.getShortName();
	}

	/**
	 * Convert to entity attribute.
	 *
	 * @param tokenShortName the token short name
	 * @return the verification token type
	 */
	@Override
	public VerificationTokenType convertToEntityAttribute(String tokenShortName) {
		if (tokenShortName == null) {
			return null;
		}
		return VerificationTokenType.fromShortName(tokenShortName);
	}

}