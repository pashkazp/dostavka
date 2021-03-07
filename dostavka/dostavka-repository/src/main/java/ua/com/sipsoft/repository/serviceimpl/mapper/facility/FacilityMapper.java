package ua.com.sipsoft.repository.serviceimpl.mapper.facility;

import java.util.List;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.dao.common.Facility;
import ua.com.sipsoft.dao.common.FacilityAddr;
import ua.com.sipsoft.repository.serviceimpl.mapper.security.UserMapper;
import ua.com.sipsoft.service.dto.facility.FacilityAddrDto;
import ua.com.sipsoft.service.dto.facility.FacilityDto;
import ua.com.sipsoft.service.dto.facility.FacilityRegReqDto;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
@Component
public interface FacilityMapper {

	FacilityDto toDto(Facility facility);

	Facility fromDto(FacilityDto facilityDto);

	@Mappings({ @Mapping(target = "facility.facilityAddresses", ignore = true) })
	FacilityAddrDto toDto(FacilityAddr facilityAddr);

	@Mappings({ @Mapping(target = "facility.facilityAddresses", ignore = true) })
	FacilityAddr fromDto(FacilityAddrDto facilityAddrDto);

	Facility fromRegDto(FacilityRegReqDto facilityRegDto);

	List<FacilityDto> toDto(List<Facility> facilities);

	List<Facility> fromDto(List<FacilityDto> facilitiesDto);

	Stream<FacilityDto> toDto(Stream<Facility> facilities);

	Stream<Facility> fromDto(Stream<FacilityDto> facilitiesDto);

}
