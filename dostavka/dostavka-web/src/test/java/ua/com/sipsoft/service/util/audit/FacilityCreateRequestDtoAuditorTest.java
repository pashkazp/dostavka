package ua.com.sipsoft.service.util.audit;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@Slf4j
@DisplayName("Testing Facility create request auditor")
class FacilityCreateRequestDtoAuditorTest {

	@Test
	@Disabled
	void test() {
		fail("Not yet implemented");
	}

}
