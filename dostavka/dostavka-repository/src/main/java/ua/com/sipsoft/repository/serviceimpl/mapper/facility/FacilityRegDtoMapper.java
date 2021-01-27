package ua.com.sipsoft.repository.serviceimpl.mapper.facility;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.dao.common.Facility;
import ua.com.sipsoft.dao.common.FacilityAddress;
import ua.com.sipsoft.service.dto.facility.FacilityRegistrationDto;

@Mapper()
public interface FacilityRegDtoMapper {

	public FacilityRegDtoMapper MAPPER = Mappers.getMapper(FacilityRegDtoMapper.class);

	public abstract Facility fromRegDto(FacilityRegistrationDto facilityRegDto);

	@AfterMapping
	default void populateFacilityAddress(FacilityRegistrationDto facilityRegDto, @MappingTarget Facility facility) {
		if (facility == null || facility.getFacilityAddresses() == null) {
			return;
		}
		FacilityAddress address = FacilityAddress.builder()
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
