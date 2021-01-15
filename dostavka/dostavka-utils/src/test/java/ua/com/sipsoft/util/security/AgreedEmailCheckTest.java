package ua.com.sipsoft.util.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

//@TestInstance(Lifecycle.PER_CLASS)
class AgreedEmailCheckTest {

	@Test
	@DisplayName("Test valid email")
	void testValidEmail() {
		assertTrue(AgreedEmailCheck.agreedEmailCheck("qqq@ukr.net"));
	}

	@ParameterizedTest
	@DisplayName("Test invalid email")
	@NullSource
	@EmptySource
	@ValueSource(strings = { "qqq@qqq.1", "@ukr.net", "qqq@qqq" })
	void test(String candidate) {
		assertFalse(AgreedEmailCheck.agreedEmailCheck(candidate));
	}

}
