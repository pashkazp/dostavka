package ua.com.sipsoft.ui.model.request.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.service.dto.facility.FacilityRegReqDto;
import ua.com.sipsoft.ui.model.request.facility.FacilityRegReq;

@Mapper(componentModel = "spring", uses = ToFacilityAddrRegDtoMapper.class)
@Component
public interface ToFacilityRegDtoMapper {

	FacilityRegReqDto fromFacilityRegReq(FacilityRegReq newFacility);
}
