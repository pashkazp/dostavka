package ua.com.sipsoft.repository.serviceimpl.mapper.facility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.dao.common.Facility;
import ua.com.sipsoft.dao.common.FacilityAddress;
import ua.com.sipsoft.repository.serviceimpl.mapper.facility.FacilityAddressMapper;
import ua.com.sipsoft.service.dto.facility.FacilityAddressDto;

//@SpringBootTest
public class FacilityAddressMapperTest {

	@Test
	@DisplayName("Map FacilityAddress to FacilityAddressDto")
	public void testToDto() {
		FacilityAddress facilityAddress = createFacilityAddress();
		FacilityAddressMapper facilityAddressMapper = Mappers.getMapper(FacilityAddressMapper.class);

		FacilityAddressDto facilityAddressDto = facilityAddressMapper.toDto(facilityAddress);

		assertEquals(facilityAddressDto.getId(), facilityAddress.getId());
		assertEquals(facilityAddressDto.getVersion(), facilityAddress.getVersion());
		assertEquals(facilityAddressDto.getAddress(), facilityAddress.getAddress());
		assertEquals(facilityAddressDto.getAddressesAlias(), facilityAddress.getAddressesAlias());
		assertEquals(facilityAddressDto.getLat(), facilityAddress.getLat());
		assertEquals(facilityAddressDto.getLng(), facilityAddress.getLng());
	}

	@Test
	@DisplayName("Map FacilityAddressDto to FacilityAddress")
	public void testFromDto() {
		FacilityAddress facilityAddress1 = createFacilityAddress();
		FacilityAddressMapper facilityAddressMapper = Mappers.getMapper(FacilityAddressMapper.class);

		FacilityAddressDto facilityAddressDto = facilityAddressMapper.toDto(facilityAddress1);
		FacilityAddress facilityAddress2 = facilityAddressMapper.fromDto(facilityAddressDto);

		assertEquals(facilityAddress2.getId(), facilityAddress1.getId());
		assertEquals(facilityAddress2.getVersion(), facilityAddress1.getVersion());
		assertEquals(facilityAddress2.getAddress(), facilityAddress1.getAddress());
		assertEquals(facilityAddress2.getAddressesAlias(), facilityAddress1.getAddressesAlias());
		assertEquals(facilityAddress2.getLat(), facilityAddress1.getLat());
		assertEquals(facilityAddress2.getLng(), facilityAddress1.getLng());
		assertNotEquals(facilityAddress2.getFacility(), facilityAddress1.getFacility());
	}

	private FacilityAddress createFacilityAddress() {
		FacilityAddress facilityAddress = new FacilityAddress("alias1", "address1");
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
