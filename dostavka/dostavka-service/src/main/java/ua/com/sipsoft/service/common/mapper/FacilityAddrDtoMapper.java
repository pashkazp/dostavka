package ua.com.sipsoft.service.common.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.service.dto.facility.FacilityAddrDto;
import ua.com.sipsoft.service.dto.facility.FacilityAddrRegReqDto;

@Mapper(componentModel = "spring")
@Component
public interface FacilityAddrDtoMapper {

	public abstract FacilityAddrDto toDto(FacilityAddrRegReqDto facilityAddrRegDto);

}
