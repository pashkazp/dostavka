package ua.com.sipsoft.ui.model.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.service.dto.facility.FacilityAddrRegDto;
import ua.com.sipsoft.ui.model.request.facility.FacilityAddressRegReq;

@Mapper
public interface ToFacilityAddressRegistrationDtoMapper {

	ToFacilityAddressRegistrationDtoMapper MAPPER = Mappers
			.getMapper(ToFacilityAddressRegistrationDtoMapper.class);

	FacilityAddrRegDto fromFacilityRegistrationRequest(FacilityAddressRegReq newAddress);
}
