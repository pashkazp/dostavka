package ua.com.sipsoft.util.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class AgreedUsernameCheckTest {

	@ParameterizedTest
	@DisplayName("Test valid user name")
	@ValueSource(strings = { "123", "asd", "Aaaaaa111", "111111Aa", "UserName", "Кто-то там" })
	void test1(String candidate) {
		assertTrue(AgreedUsernameCheck.agreedUsernameCheck(candidate));
	}

	@ParameterizedTest
	@DisplayName("Test invalid user name")
	@NullSource
	@EmptySource
	@ValueSource(strings = { " ", "12", "us" })
	void tes2(String candidate) {
		assertFalse(AgreedUsernameCheck.agreedUsernameCheck(candidate));
	}

}
