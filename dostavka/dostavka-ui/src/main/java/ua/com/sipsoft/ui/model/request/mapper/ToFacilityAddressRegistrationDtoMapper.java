package ua.com.sipsoft.ui.model.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.service.dto.facility.FacilityAddressRegistrationDto;
import ua.com.sipsoft.ui.model.request.facility.FacilityAddressRegReq;

@Mapper
public interface ToFacilityAddressRegistrationDtoMapper {

	ToFacilityAddressRegistrationDtoMapper MAPPER = Mappers
			.getMapper(ToFacilityAddressRegistrationDtoMapper.class);

	FacilityAddressRegistrationDto fromFacilityRegistrationRequest(FacilityAddressRegReq newAddress);
}
