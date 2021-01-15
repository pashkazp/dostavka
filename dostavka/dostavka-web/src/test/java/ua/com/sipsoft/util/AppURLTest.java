package ua.com.sipsoft.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ua.com.sipsoft.web.DostavkaApplicationTest;

class AppURLTest extends DostavkaApplicationTest {

	@Test
	@DisplayName("Check the set variables of AppURL.")
	void test() {

		assertAll(() -> assertTrue(StringUtils.isNotBlank(AppURL.APP_DOMAIN)));
	}

}
