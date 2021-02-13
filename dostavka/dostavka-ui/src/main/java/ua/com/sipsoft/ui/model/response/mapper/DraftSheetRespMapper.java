package ua.com.sipsoft.ui.model.response.mapper;

import java.util.List;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.service.dto.request.RouteSheetDto;
import ua.com.sipsoft.ui.model.response.request.daft.DraftSheetCardResponse;

@Mapper(componentModel = "spring")
@Component
public interface DraftSheetRespMapper {

	@Mapping(target = "author", source = "author.name")
	DraftSheetCardResponse toRest(RouteSheetDto routeSheet);

	List<DraftSheetCardResponse> toRest(List<RouteSheetDto> routeSheets);

	Stream<DraftSheetCardResponse> toRest(Stream<RouteSheetDto> routeSheets);

}
