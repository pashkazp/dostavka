package ua.com.sipsoft.ui.model.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.service.dto.facility.FacilityRegReqDto;
import ua.com.sipsoft.ui.model.request.facility.FacilityRegReq;

@Mapper(uses = ToFacilityAddrRegDtoMapper.class)
public interface ToFacilityRegDtoMapper {

	ToFacilityRegDtoMapper MAPPER = Mappers
			.getMapper(ToFacilityRegDtoMapper.class);

	FacilityRegReqDto fromFacilityRegReq(FacilityRegReq newFacility);
}
