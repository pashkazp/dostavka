package ua.com.sipsoft.ui.model.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.service.dto.facility.FacilityAddrUpdReqDto;
import ua.com.sipsoft.ui.model.request.facility.FacilityAddrUpdReq;

@Mapper
public interface ToFacilityAddrUpdDtoMapper {

	ToFacilityAddrUpdDtoMapper MAPPER = Mappers
			.getMapper(ToFacilityAddrUpdDtoMapper.class);

	FacilityAddrUpdReqDto fromFacilityAddrUpdReq(FacilityAddrUpdReq updAddr);
}
