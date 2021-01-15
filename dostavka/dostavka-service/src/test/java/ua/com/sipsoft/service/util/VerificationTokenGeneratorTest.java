package ua.com.sipsoft.service.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

@Component
class VerificationTokenGeneratorTest implements VerificationTokenGenerator {

	@Test
	@DisplayName("Generate random UUID")
	void test() {
		String token = makeVerificationToken();
		assertFalse(StringUtils.isBlank(token));
		assertTrue(token.length() > 35);
	}

}
