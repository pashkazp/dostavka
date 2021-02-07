package ua.com.sipsoft.repository.serviceimpl.mapper.facility;

import java.util.List;
import java.util.stream.Stream;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.dao.common.Facility;
import ua.com.sipsoft.dao.common.FacilityAddr;
import ua.com.sipsoft.repository.serviceimpl.mapper.security.UserMapper;
import ua.com.sipsoft.service.dto.facility.FacilityDto;
import ua.com.sipsoft.service.dto.facility.FacilityRegReqDto;

@Mapper(uses = { FacilityAddMapper.class, UserMapper.class })
public interface FacilityMapper {

	public FacilityMapper MAPPER = Mappers.getMapper(FacilityMapper.class);

	// @Mapping(target = "facilityAddresses", ignore = true)
	public abstract FacilityDto toDto(Facility facility);

	// @Mapping(target = "facilityAddresses", ignore = true)
	public abstract Facility fromDto(FacilityDto facilityDto);

	public abstract Facility fromRegDto(FacilityRegReqDto facilityRegDto);

	public abstract List<FacilityDto> toDto(List<Facility> facilities);

	public abstract List<Facility> fromDto(List<FacilityDto> facilitiesDto);

	public abstract Stream<FacilityDto> toDto(Stream<Facility> facilities);

	public abstract Stream<Facility> fromDto(Stream<FacilityDto> facilitiesDto);

	@AfterMapping
	default void populateFacilityAddress(@MappingTarget Facility facility) {
		if (facility == null || facility.getFacilityAddresses() == null) {
			return;
		}
		for (FacilityAddr facilityAddress : facility.getFacilityAddresses()) {
			facilityAddress.setFacility(facility);
		}
	}

}
