package ua.com.sipsoft.service.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.service.dto.facility.FacilityAddrRegReqDto;
import ua.com.sipsoft.service.dto.facility.FacilityAddrDto;

@Mapper()
public interface FacilityAddressDtoMapper {

	public FacilityAddressDtoMapper MAPPER = Mappers.getMapper(FacilityAddressDtoMapper.class);

	public abstract FacilityAddrDto toDto(FacilityAddrRegReqDto facilityAddrRegDto);

}
