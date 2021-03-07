package ua.com.sipsoft.repository.serviceimpl.mapper.facility;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.dao.common.Facility;
import ua.com.sipsoft.dao.common.FacilityAddr;
import ua.com.sipsoft.service.dto.facility.FacilityAddrDto;
import ua.com.sipsoft.service.dto.facility.FacilityAddrRegReqDto;
import ua.com.sipsoft.service.dto.facility.FacilityAddrUpdReqDto;
import ua.com.sipsoft.service.dto.facility.FacilityDto;

@Mapper(componentModel = "spring")
@Component
public interface FacilityAddMapper {

	FacilityAddrDto toDto(FacilityAddr facilityAddress);

	FacilityAddr fromDto(FacilityAddrDto facilityAddressDto);

	@Mappings({ @Mapping(target = "facilityAddresses", ignore = true) })
	FacilityDto toDto(Facility facility);

	@Mappings({ @Mapping(target = "facilityAddresses", ignore = true) })
	Facility fromDto(FacilityDto facilityDto);

	FacilityAddr fromDto(FacilityAddrRegReqDto facilityAddrRegDto);

	FacilityAddr fromDto(FacilityAddrUpdReqDto facilityAddrUpdReqDto);

	Set<FacilityAddr> fromDto(Collection<FacilityAddrDto> facilityAddressDtos);

	List<FacilityAddrDto> toDto(List<FacilityAddr> facilityAddresses);

	List<FacilityAddr> fromDto(List<FacilityAddrDto> facilityAddressDto);

}
