package ua.com.sipsoft.dao.util;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import ua.com.sipsoft.util.security.VerificationTokenType;

class VerificationTokenConvertorTest {

	VerificationTokenConvertor convertor = new VerificationTokenConvertor();

	@DisplayName("Test convertion of string value to VerificationTokenType.REGNEWUSER ")
	@ParameterizedTest
	@ValueSource(strings = { "RNU", "Rnu", "rnu" })
	void test1(String entityValue) {
		assertEquals(VerificationTokenType.REGNEWUSER, convertor.convertToEntityAttribute(entityValue));
	}

	@DisplayName("Test convertion of string value to VerificationTokenType.FORGOTPASS ")
	@ParameterizedTest
	@ValueSource(strings = { "FGP", "Fgp", "fgp" })
	void test2(String entityValue) {
		assertEquals(VerificationTokenType.FORGOTPASS, convertor.convertToEntityAttribute(entityValue));
	}

	@DisplayName("Test convertion failure of string value to VerificationTokenType")
	@ParameterizedTest
	@EmptySource
	@ValueSource(strings = { "FP", " fgp " })
	void test3(String entityValue) {
		assertThatThrownBy(() -> convertor
				.convertToEntityAttribute(entityValue))
						.isInstanceOf(UnsupportedOperationException.class);
	}

	@DisplayName("Test convertion null string value to null")
	@ParameterizedTest
	@NullSource
	void test4(String entityValue) {
		assertNull(convertor.convertToEntityAttribute(entityValue));
	}

	@Test
	@DisplayName("Test convertion fo VerificationTokenType to string value")
	void test5() {
		assertEquals("RNU", convertor.convertToDatabaseColumn(VerificationTokenType.REGNEWUSER));
		assertEquals("FGP", convertor.convertToDatabaseColumn(VerificationTokenType.FORGOTPASS));
		assertNull(convertor.convertToDatabaseColumn(null));
	}

}
