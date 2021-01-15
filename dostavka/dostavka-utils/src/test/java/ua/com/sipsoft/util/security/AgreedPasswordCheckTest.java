package ua.com.sipsoft.util.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class AgreedPasswordCheckTest {

	@ParameterizedTest
	@DisplayName("Test valid password")
	@ValueSource(strings = { "PasswordPassword#1", "PasswordPassword1", "Aaaaaa111", "PasPas#1", "111111Aa",
			"Aa1@#$%^&+=!\"№;" })
	void test1(String candidate) {
		assertTrue(AgreedPasswordCheck.adreedPasswordCheck(candidate));
	}

	@ParameterizedTest
	@DisplayName("Test invalid password")
	@NullSource
	@EmptySource
	@ValueSource(strings = { " ", "111111111", "PasPa#1", "paspas#1", "PASPAS#1", "PasswordPassword#", "password",
			"Pass word1", "Pass\tword1", "PasswordPassword1qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq" })
	void tes2(String candidate) {
		assertFalse(AgreedPasswordCheck.adreedPasswordCheck(candidate));
	}

}
