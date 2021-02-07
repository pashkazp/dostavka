package ua.com.sipsoft.ui.model.response.mapper;

import java.util.List;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.service.dto.facility.FacilityDto;
import ua.com.sipsoft.ui.model.response.facility.FacilityResponse;

@Mapper(componentModel = "spring")
@Component
public interface FacilityRespMapper {

	FacilityResponse toRest(FacilityDto facilityDto);

	List<FacilityResponse> toRest(List<FacilityDto> facilities);

	Stream<FacilityResponse> toRest(Stream<FacilityDto> facilities);

}
