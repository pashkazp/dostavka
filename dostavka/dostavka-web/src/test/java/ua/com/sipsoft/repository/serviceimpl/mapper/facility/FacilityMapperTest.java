package ua.com.sipsoft.repository.serviceimpl.mapper.facility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.sipsoft.dao.common.Facility;
import ua.com.sipsoft.dao.common.FacilityAddr;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.dto.facility.FacilityDto;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class FacilityMapperTest {

	@Autowired
	FacilityMapper facilityMapper;

	@Test
	@DisplayName("Map Facility to FacilityDto")
	public void testToDto() {
		Facility facility = createFacility();
		FacilityDto facilityDto = facilityMapper.toDto(facility);

		assertEquals(facilityDto.getId(), facility.getId());
		assertEquals(facilityDto.getVersion(), facility.getVersion());
		assertEquals(facilityDto.getName(), facility.getName());
		// assertNotNull(facilityDto.getFacilityAddresses());
		// assertEquals(facilityDto.getFacilityAddresses().size(),
		// facility.getFacilityAddresses().size());

		assertNotNull(facilityDto.getUsers());
		assertEquals(facilityDto.getUsers().size(), facility.getUsers().size());
	}

	@Test
	@DisplayName("Map FacilityDto to Facility")
	public void testFromDto() {
		Facility facility1 = createFacility();
		FacilityDto facilityDto = facilityMapper.toDto(facility1);
		Facility facility2 = facilityMapper.fromDto(facilityDto);

		assertEquals(facility2.getId(), facility1.getId());
		assertEquals(facility2.getVersion(), facility1.getVersion());
		assertEquals(facility2.getName(), facility1.getName());
		// assertNotNull(facility2.getFacilityAddresses());
		// assertEquals(facility2.getFacilityAddresses().size(),
		// facility1.getFacilityAddresses().size());

		assertNotNull(facility2.getUsers());
		assertEquals(facility2.getUsers().size(), facility1.getUsers().size());
	}

	/**
	 * Spare method for creates the facility.
	 *
	 * @return the facility
	 */
	private Facility createFacility() {
		FacilityAddr facilityAddress1 = new FacilityAddr("alias1", "address1");
		facilityAddress1.setId(1L);
		facilityAddress1.setVersion(1L);
		facilityAddress1.setDefaultAddress(true);
		facilityAddress1.setLat(1.1d);
		facilityAddress1.setLng(2.2d);

		FacilityAddr facilityAddress2 = new FacilityAddr("alias2", "address2");
		facilityAddress1.setId(2L);
		facilityAddress1.setVersion(2L);
		facilityAddress1.setDefaultAddress(false);
		facilityAddress1.setLat(1.1d);
		facilityAddress1.setLng(2.2d);

		User user = new User();
		user.setName("User Name");
		user.setName("User Name");

		Facility facility = new Facility();
		facility.setId(1L);
		facility.setName("name");
		facility.setVersion(0L);

		facility.addUser(user);
		facility.addFacilityAddress(facilityAddress1);
		facility.addFacilityAddress(facilityAddress2);

		return facility;
	}

}
