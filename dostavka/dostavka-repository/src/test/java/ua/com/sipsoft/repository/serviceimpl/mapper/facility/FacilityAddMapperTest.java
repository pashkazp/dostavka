package ua.com.sipsoft.repository.serviceimpl.mapper.facility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.dao.common.Facility;
import ua.com.sipsoft.dao.common.FacilityAddr;
import ua.com.sipsoft.repository.serviceimpl.mapper.facility.FacilityAddMapper;
import ua.com.sipsoft.service.dto.facility.FacilityAddrDto;

//@SpringBootTest
public class FacilityAddMapperTest {

	@Test
	@DisplayName("Map FacilityAddr to FacilityAddrDto")
	public void testToDto() {
		FacilityAddr facilityAddress = createFacilityAddress();
		FacilityAddMapper facilityAddressMapper = Mappers.getMapper(FacilityAddMapper.class);

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
		FacilityAddMapper facilityAddressMapper = Mappers.getMapper(FacilityAddMapper.class);

		FacilityAddrDto facilityAddressDto = facilityAddressMapper.toDto(facilityAddress1);
		FacilityAddr facilityAddress2 = facilityAddressMapper.fromDto(facilityAddressDto);

		assertEquals(facilityAddress2.getId(), facilityAddress1.getId());
		assertEquals(facilityAddress2.getVersion(), facilityAddress1.getVersion());
		assertEquals(facilityAddress2.getAddress(), facilityAddress1.getAddress());
		assertEquals(facilityAddress2.getAddressesAlias(), facilityAddress1.getAddressesAlias());
		assertEquals(facilityAddress2.getLat(), facilityAddress1.getLat());
		assertEquals(facilityAddress2.getLng(), facilityAddress1.getLng());
		assertNotEquals(facilityAddress2.getFacility(), facilityAddress1.getFacility());
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
