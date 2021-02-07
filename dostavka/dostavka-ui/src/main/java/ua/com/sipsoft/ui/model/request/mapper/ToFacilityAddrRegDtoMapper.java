package ua.com.sipsoft.ui.model.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.service.dto.facility.FacilityAddrRegReqDto;
import ua.com.sipsoft.ui.model.request.facility.FacilityAddrRegReq;

@Mapper
public interface ToFacilityAddrRegDtoMapper {

	ToFacilityAddrRegDtoMapper MAPPER = Mappers
			.getMapper(ToFacilityAddrRegDtoMapper.class);

	FacilityAddrRegReqDto fromFacilityRegReq(FacilityAddrRegReq newAddress);
}
