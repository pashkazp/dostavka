package ua.com.sipsoft.service.common.mapper;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.service.dto.facility.FacilityAddrDto;
import ua.com.sipsoft.service.dto.facility.FacilityAddrRegReqDto;

@Mapper(componentModel = "spring")
@Component
public interface FacilityAddrDtoMapper {

	@Mapping(source = "lat", target = "lat", qualifiedByName = "toDoubleTransformation")
	@Mapping(source = "lng", target = "lng", qualifiedByName = "toDoubleTransformation")
	public abstract FacilityAddrDto toDto(FacilityAddrRegReqDto facilityAddrRegDto);

	@Named("toDoubleTransformation")
	default Double myCustomTransformation(String obj) {
		return StringUtils.isBlank(obj) ? null : Double.valueOf(obj);
	}

}
