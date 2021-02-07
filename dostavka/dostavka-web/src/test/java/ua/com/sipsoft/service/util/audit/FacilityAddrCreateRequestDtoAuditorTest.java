package ua.com.sipsoft.service.util.audit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.service.dto.facility.FacilityAddrRegReqDto;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@Slf4j
@DisplayName("Testing Facility address create request auditor")
class FacilityAddrCreateRequestDtoAuditorTest {

	@Autowired
	FacilityAddrCreateRequestDtoAuditor auditor;

	Locale locale = new Locale("en-US");

	@Test
	@DisplayName("test return valid/invalid methods")
	public void test2() {
		FacilityAddrRegReqDto inspectedInfo = null;
		assertEquals(auditor.inspectNewData(inspectedInfo, null, locale).isInvalid(),
				!auditor.inspectNewData(inspectedInfo, null, locale).isValid());
	}

	@Test
	@DisplayName("test wery long address")
	public void test5() {
		FacilityAddrRegReqDto inspectedInfo = FacilityAddrRegReqDto.builder().address("*".repeat(256)).build();
		assertFalse(auditor.inspectNewData(inspectedInfo, null, locale).isValid());
	}

	@ParameterizedTest
	@MethodSource("trueAddresses")
	@DisplayName("test for true facility address request")
	public void test6(FacilityAddrRegReqDto inspectedInfo) {
		assertTrue(auditor.inspectNewData(inspectedInfo, null, locale).isValid());
	}

	private static Stream<FacilityAddrRegReqDto> trueAddresses() {
		return Stream.of(FacilityAddrRegReqDto.builder().address("*").build(),
				FacilityAddrRegReqDto.builder().address("*".repeat(2)).build(),
				FacilityAddrRegReqDto.builder().address("*".repeat(255)).build(),
				FacilityAddrRegReqDto.builder().address("*").lat("").build(),
				FacilityAddrRegReqDto.builder().address("*").lng("").build(),
				FacilityAddrRegReqDto.builder().address("*").lat("-90").build(),
				FacilityAddrRegReqDto.builder().address("*").lng("-180").build(),
				FacilityAddrRegReqDto.builder().address("*").lat("90").build(),
				FacilityAddrRegReqDto.builder().address("*").lng("180").build(),
				FacilityAddrRegReqDto.builder().address("*").addressesAlias("*".repeat(100)).build(),
				FacilityAddrRegReqDto.builder().address("*").addressesAlias("").build());
	}

	@ParameterizedTest
	@MethodSource("falseAddresses")
	@DisplayName("test for wrong facility address request")
	public void test7(FacilityAddrRegReqDto inspectedInfo) {
		assertTrue(auditor.inspectNewData(inspectedInfo, null, locale).isInvalid());
	}

	private static Stream<FacilityAddrRegReqDto> falseAddresses() {
		return Stream.of(null,
				FacilityAddrRegReqDto.builder().address("").build(),
				FacilityAddrRegReqDto.builder().address("*".repeat(256)).build(),
				FacilityAddrRegReqDto.builder().addressesAlias("*").build(),
				FacilityAddrRegReqDto.builder().address("*").addressesAlias("*".repeat(101)).build(),
				FacilityAddrRegReqDto.builder().address("*").lat("sdf").build(),
				FacilityAddrRegReqDto.builder().address("*").lng("sdf").build(),
				FacilityAddrRegReqDto.builder().address("*").lat("-91").build(),
				FacilityAddrRegReqDto.builder().address("*").lng("-181").build(),
				FacilityAddrRegReqDto.builder().address("*").lat("90.0001").build(),
				FacilityAddrRegReqDto.builder().address("*").lng("180.0001").build());
	}

}
