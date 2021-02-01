package ua.com.sipsoft.repository.serviceimpl.mapper.facility;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.dao.common.FacilityAddress;
import ua.com.sipsoft.service.dto.facility.FacilityAddrRegDto;
import ua.com.sipsoft.service.dto.facility.FacilityAddressDto;

@Mapper(uses = FacilityMapper.class)
public interface FacilityAddressMapper {

	public FacilityAddressMapper MAPPER = Mappers.getMapper(FacilityAddressMapper.class);

	// @Mapping(target = "facility", ignore = true)
	public abstract FacilityAddressDto toDto(FacilityAddress facilityAddress);

	// @Mapping(target = "facility", ignore = true)
	public abstract FacilityAddress fromDto(FacilityAddressDto facilityAddressDto);

	public abstract FacilityAddress fromDto(FacilityAddrRegDto facilityAddrRegDto);

	public abstract Set<FacilityAddress> fromDto(Collection<FacilityAddressDto> facilityAddressDtos);

	public abstract List<FacilityAddressDto> toDto(List<FacilityAddress> facilityAddresses);

	public abstract List<FacilityAddress> fromDto(List<FacilityAddressDto> facilityAddressDto);

}
