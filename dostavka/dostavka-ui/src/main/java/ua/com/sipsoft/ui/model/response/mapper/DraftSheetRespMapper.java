package ua.com.sipsoft.ui.model.response.mapper;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.service.dto.request.RouteSheetDto;
import ua.com.sipsoft.ui.model.response.request.draft.DraftSheetCardResp;

@Mapper(componentModel = "spring")
@Component
public interface DraftSheetRespMapper {

	final static DateTimeFormatter format = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS");

	@Mapping(target = "author", source = "author.name")
	// @Mapping(expression = "java(routeSheet.getCreationDate().format(format))",
	// target = "creationDate")
	DraftSheetCardResp toRest(RouteSheetDto routeSheet);

	List<DraftSheetCardResp> toRest(List<RouteSheetDto> routeSheets);

	Stream<DraftSheetCardResp> toRest(Stream<RouteSheetDto> routeSheets);

}
