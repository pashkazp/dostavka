package ua.com.sipsoft.ui.model.response.mapper;

import java.util.List;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.service.dto.request.CourierRequestDto;
import ua.com.sipsoft.ui.model.response.request.draft.DraftReqsCardResponse;

@Mapper(componentModel = "spring")
@Component
public interface DraftReqsRespMapper {

	@Mappings(value = {
			@Mapping(target = "author", source = "author.name"),
			@Mapping(expression = "java(routeReqs.getFromPoint().getId())", target = "fromId"),
			@Mapping(expression = "java(routeReqs.getToPoint().getId())", target = "toId"),
			@Mapping(expression = "java(routeReqs.getFromPoint().getAddress())", target = "fromAddr"),
			@Mapping(expression = "java(routeReqs.getToPoint().getAddress())", target = "toAddr"),
			@Mapping(expression = "java(routeReqs.getFromPoint().getFacility().getName())", target = "fromFacility"),
			@Mapping(expression = "java(routeReqs.getToPoint().getFacility().getName())", target = "toFacility")
	})
	DraftReqsCardResponse toRest(CourierRequestDto routeReqs);

	List<DraftReqsCardResponse> toRest(List<CourierRequestDto> routeReqs);

	Stream<DraftReqsCardResponse> toRest(Stream<CourierRequestDto> routeReqs);

}
