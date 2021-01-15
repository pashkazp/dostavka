package ua.com.sipsoft.ui.model.response.mapper;

import java.util.List;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.service.dto.facility.FacilityDto;
import ua.com.sipsoft.ui.model.response.facility.FacilityResponse;

@Mapper
public interface FacilityRespMapper {

	FacilityRespMapper MAPPER = Mappers.getMapper(FacilityRespMapper.class);

	FacilityResponse toRest(FacilityDto facilityDto);

	List<FacilityResponse> toRest(List<FacilityDto> facilities);

	Stream<FacilityResponse> toRest(Stream<FacilityDto> facilities);

}
