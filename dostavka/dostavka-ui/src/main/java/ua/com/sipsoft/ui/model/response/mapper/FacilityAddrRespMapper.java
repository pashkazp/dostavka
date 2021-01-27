package ua.com.sipsoft.ui.model.response.mapper;

import java.util.List;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.service.dto.facility.FacilityAddressDto;
import ua.com.sipsoft.ui.model.response.facility.FacilityAddrResponse;

@Mapper
public interface FacilityAddrRespMapper {

	FacilityAddrRespMapper MAPPER = Mappers.getMapper(FacilityAddrRespMapper.class);

	FacilityAddrResponse toRest(FacilityAddressDto facilityDto);

	List<FacilityAddrResponse> toRest(List<FacilityAddressDto> facilities);

	Stream<FacilityAddrResponse> toRest(Stream<FacilityAddressDto> facilities);

}
