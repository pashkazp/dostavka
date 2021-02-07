package ua.com.sipsoft.ui.model.request.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.service.dto.facility.FacilityUpdReqDto;
import ua.com.sipsoft.ui.model.request.facility.FacilityUpdReq;

@Mapper(componentModel = "spring")
@Component
public interface ToFacilityUpdDtoMapper {

	FacilityUpdReqDto fromFacilityUpdReq(FacilityUpdReq updFacility);
}
