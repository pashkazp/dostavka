package ua.com.sipsoft.repository.serviceimpl.mapper.facility;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.dao.common.Facility;
import ua.com.sipsoft.dao.common.FacilityAddr;
import ua.com.sipsoft.service.dto.facility.FacilityRegReqDto;

@Mapper(componentModel = "spring")
@Component
public interface FacilityRegDtoMapper {

	public abstract Facility fromRegDto(FacilityRegReqDto facilityRegDto);

	/**
	 * Populate facility address. Add Facility address to the Addresses List in
	 * Facility if the Address in the source object is exist and is not empty
	 *
	 * @param facilityRegDto the facility registration request that is source object
	 * @param facility       the Facility that is target object
	 */
	@AfterMapping
	default void populateFacilityAddress(FacilityRegReqDto facilityRegDto, @MappingTarget Facility facility) {
		if (facility == null
				|| facility.getFacilityAddresses() == null
				|| facilityRegDto.getFacilityAddress() == null
				|| facilityRegDto.getFacilityAddress().isEmpty()) {
			return;
		}

		FacilityAddr address = FacilityAddr.builder()
				.address(facilityRegDto.getFacilityAddress().getAddress())
				.addressesAlias(facilityRegDto.getFacilityAddress().getAddressesAlias())
				.defaultAddress(facilityRegDto.getFacilityAddress().isDefaultAddress())
				.build();

		if (StringUtils.length(facilityRegDto.getFacilityAddress().getLat()) > 0) {
			try {
				Double d = Double.valueOf(facilityRegDto.getFacilityAddress().getLat());
				address.setLat(d);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

		}
		if (StringUtils.length(facilityRegDto.getFacilityAddress().getLng()) > 0) {
			try {
				Double d = Double.valueOf(facilityRegDto.getFacilityAddress().getLng());
				address.setLng(d);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		facility.addFacilityAddress(address);
	}

}
