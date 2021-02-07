package ua.com.sipsoft.ui.model.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.service.dto.facility.FacilityUpdReqDto;
import ua.com.sipsoft.ui.model.request.facility.FacilityUpdReq;

@Mapper
public interface ToFacilityUpdDtoMapper {

	ToFacilityUpdDtoMapper MAPPER = Mappers
			.getMapper(ToFacilityUpdDtoMapper.class);

	FacilityUpdReqDto fromFacilityUpdReq(FacilityUpdReq updFacility);
}
