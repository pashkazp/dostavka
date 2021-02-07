package ua.com.sipsoft.ui.model.request.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.service.dto.facility.FacilityAddrRegReqDto;
import ua.com.sipsoft.ui.model.request.facility.FacilityAddrRegReq;

@Mapper(componentModel = "spring")
@Component
public interface ToFacilityAddrRegDtoMapper {

	FacilityAddrRegReqDto fromFacilityRegReq(FacilityAddrRegReq newAddress);
}
