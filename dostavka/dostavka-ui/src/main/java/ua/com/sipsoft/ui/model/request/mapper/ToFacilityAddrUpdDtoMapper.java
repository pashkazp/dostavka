package ua.com.sipsoft.ui.model.request.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.service.dto.facility.FacilityAddrUpdReqDto;
import ua.com.sipsoft.ui.model.request.facility.FacilityAddrUpdReq;

@Mapper(componentModel = "spring")
@Component
public interface ToFacilityAddrUpdDtoMapper {

	FacilityAddrUpdReqDto fromFacilityAddrUpdReq(FacilityAddrUpdReq updAddr);
}
