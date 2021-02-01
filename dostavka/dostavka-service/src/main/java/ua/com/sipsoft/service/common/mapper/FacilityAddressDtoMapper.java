package ua.com.sipsoft.service.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.service.dto.facility.FacilityAddrRegDto;
import ua.com.sipsoft.service.dto.facility.FacilityAddressDto;

@Mapper()
public interface FacilityAddressDtoMapper {

	public FacilityAddressDtoMapper MAPPER = Mappers.getMapper(FacilityAddressDtoMapper.class);

	public abstract FacilityAddressDto toDto(FacilityAddrRegDto facilityAddrRegDto);

}
