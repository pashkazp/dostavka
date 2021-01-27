package ua.com.sipsoft.ui.model.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.service.dto.facility.FacilityRegistrationDto;
import ua.com.sipsoft.ui.model.request.facility.FacilityRegistrationRequest;

@Mapper(uses = ToFacilityAddressRegistrationDtoMapper.class)
public interface ToFacilityRegistrationDtoMapper {

	ToFacilityRegistrationDtoMapper MAPPER = Mappers
			.getMapper(ToFacilityRegistrationDtoMapper.class);

	FacilityRegistrationDto fromFacilityRegistrationRequest(FacilityRegistrationRequest newFacility);
}
