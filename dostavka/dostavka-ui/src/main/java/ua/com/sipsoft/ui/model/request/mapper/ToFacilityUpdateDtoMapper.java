package ua.com.sipsoft.ui.model.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.service.dto.facility.FacilityUpdateDto;
import ua.com.sipsoft.ui.model.request.facility.FacilityUpdateRequest;

@Mapper
public interface ToFacilityUpdateDtoMapper {

	ToFacilityUpdateDtoMapper MAPPER = Mappers
			.getMapper(ToFacilityUpdateDtoMapper.class);

	FacilityUpdateDto fromFacilityUpdateRequest(FacilityUpdateRequest updFacility);
}
