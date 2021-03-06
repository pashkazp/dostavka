package ua.com.sipsoft.repository.serviceimpl.mapper.facility;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.dao.common.FacilityAddr;
import ua.com.sipsoft.service.dto.facility.FacilityAddrDto;
import ua.com.sipsoft.service.dto.facility.FacilityAddrRegReqDto;
import ua.com.sipsoft.service.dto.facility.FacilityAddrUpdReqDto;

@Mapper(componentModel = "spring", uses = FacilityMapper.class)
@Component
public interface FacilityAddMapper {

	// @Mapping(target = "facility", ignore = true)
	public abstract FacilityAddrDto toDto(FacilityAddr facilityAddress);

	// @Mapping(target = "facility", ignore = true)
	public abstract FacilityAddr fromDto(FacilityAddrDto facilityAddressDto);

	public abstract FacilityAddr fromDto(FacilityAddrRegReqDto facilityAddrRegDto);

	public abstract FacilityAddr fromDto(FacilityAddrUpdReqDto facilityAddrUpdReqDto);

	public abstract Set<FacilityAddr> fromDto(Collection<FacilityAddrDto> facilityAddressDtos);

	public abstract List<FacilityAddrDto> toDto(List<FacilityAddr> facilityAddresses);

	public abstract List<FacilityAddr> fromDto(List<FacilityAddrDto> facilityAddressDto);

}
