package ua.com.sipsoft.repository.serviceimpl.mapper.facility;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.sipsoft.dao.common.Facility;
import ua.com.sipsoft.dao.common.FacilityAddr;
import ua.com.sipsoft.service.dto.facility.FacilityAddrDto;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class FacilityAddMapperTest {
	@Autowired
	FacilityAddMapper facilityAddressMapper;

	@Test
	@DisplayName("Map FacilityAddr to FacilityAddrDto")
	public void testToDto() {
		FacilityAddr facilityAddress = createFacilityAddress();

		FacilityAddrDto facilityAddressDto = facilityAddressMapper.toDto(facilityAddress);

		assertEquals(facilityAddressDto.getId(), facilityAddress.getId());
		assertEquals(facilityAddressDto.getVersion(), facilityAddress.getVersion());
		assertEquals(facilityAddressDto.getAddress(), facilityAddress.getAddress());
		assertEquals(facilityAddressDto.getAddressesAlias(), facilityAddress.getAddressesAlias());
		assertEquals(facilityAddressDto.getLat(), facilityAddress.getLat());
		assertEquals(facilityAddressDto.getLng(), facilityAddress.getLng());
	}

	@Test
	@DisplayName("Map FacilityAddrDto to FacilityAddr")
	public void testFromDto() {
		FacilityAddr facilityAddress1 = createFacilityAddress();

		FacilityAddrDto facilityAddressDto = facilityAddressMapper.toDto(facilityAddress1);
		FacilityAddr facilityAddress2 = facilityAddressMapper.fromDto(facilityAddressDto);

		assertEquals(facilityAddress2.getId(), facilityAddress1.getId());
		assertEquals(facilityAddress2.getVersion(), facilityAddress1.getVersion());
		assertEquals(facilityAddress2.getAddress(), facilityAddress1.getAddress());
		assertEquals(facilityAddress2.getAddressesAlias(), facilityAddress1.getAddressesAlias());
		assertEquals(facilityAddress2.getLat(), facilityAddress1.getLat());
		assertEquals(facilityAddress2.getLng(), facilityAddress1.getLng());
		assertEquals(facilityAddress2.getFacility(), facilityAddress1.getFacility());
	}

	private FacilityAddr createFacilityAddress() {
		FacilityAddr facilityAddress = new FacilityAddr("alias1", "address1");
		facilityAddress.setId(1L);
		facilityAddress.setVersion(1L);
		facilityAddress.setDefaultAddress(true);
		facilityAddress.setLat(1.1d);
		facilityAddress.setLng(2.2d);

		Facility facility = new Facility();
		facility.setId(1L);
		facility.setName("name");
		facility.setVersion(0L);

		facility.addFacilityAddress(facilityAddress);

		return facilityAddress;
	}

}
