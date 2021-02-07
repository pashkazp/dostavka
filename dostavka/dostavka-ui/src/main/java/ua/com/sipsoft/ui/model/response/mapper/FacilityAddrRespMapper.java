package ua.com.sipsoft.ui.model.response.mapper;

import java.util.List;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.service.dto.facility.FacilityAddrDto;
import ua.com.sipsoft.ui.model.response.facility.FacilityAddrResponse;

@Mapper(componentModel = "spring")
@Component
public interface FacilityAddrRespMapper {

	FacilityAddrResponse toRest(FacilityAddrDto facilityAddrDto);

	List<FacilityAddrResponse> toRest(List<FacilityAddrDto> facilityAddrs);

	Stream<FacilityAddrResponse> toRest(Stream<FacilityAddrDto> facilityAddrs);

}
