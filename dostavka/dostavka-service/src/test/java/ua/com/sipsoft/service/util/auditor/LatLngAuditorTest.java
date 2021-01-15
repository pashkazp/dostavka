package ua.com.sipsoft.service.util.auditor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ua.com.sipsoft.service.util.audit.LatLngAuditor;

class LatLngAuditorTest implements LatLngAuditor {

	@DisplayName("Test Latitude on Double inbound range")
	@ParameterizedTest
	@ValueSource(doubles = { -90d, -89.99999999999999d, 89.99999999999999d, 90d, -Double.MIN_VALUE, Double.MIN_VALUE,
			0d, 10.123123d, -50.1231232d })
	void test1(Double coordinate) {
		assertTrue(isLatitudeAgreed(coordinate, true));
		assertTrue(isLatitudeAgreed(coordinate, false));
	}

	@DisplayName("Test Latitude on Double outbound range")
	@ParameterizedTest
	@ValueSource(doubles = { Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, -Double.MAX_VALUE,
			Double.MAX_VALUE, -91d, -90.00000000000001d, 90.00000000000001d, 91d })
	void test2(Double coordinate) {
		assertFalse(isLatitudeAgreed(coordinate, true));
		assertFalse(isLatitudeAgreed(coordinate, false));
	}

	@DisplayName("Test Longitude on Double inbound range")
	@ParameterizedTest
	@ValueSource(doubles = { -180d, -179.99999999999999d, 179.99999999999999d, 180d, -Double.MIN_VALUE,
			Double.MIN_VALUE,
			0d, 10.123123d, -50.1231232d })
	void test4(Double coordinate) {
		assertTrue(isLongitudeAgreed(coordinate, true));
		assertTrue(isLongitudeAgreed(coordinate, false));
	}

	@DisplayName("Test Longitude on Double outbound range")
	@ParameterizedTest
	@ValueSource(doubles = { Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, -Double.MAX_VALUE,
			Double.MAX_VALUE, -181d, -180.00000000000002d, 180.00000000000002d, 181d })
	void test5(Double coordinate) {
		assertFalse(isLongitudeAgreed(coordinate, true));
		assertFalse(isLongitudeAgreed(coordinate, false));
	}

}
