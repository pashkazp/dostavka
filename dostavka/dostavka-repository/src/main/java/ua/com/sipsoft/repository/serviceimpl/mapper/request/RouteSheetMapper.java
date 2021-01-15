package ua.com.sipsoft.repository.serviceimpl.mapper.request;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.dao.request.archive.ArchivedRouteSheet;
import ua.com.sipsoft.dao.request.draft.DraftRouteSheet;
import ua.com.sipsoft.dao.request.issued.IssuedRouteSheet;
import ua.com.sipsoft.repository.serviceimpl.mapper.security.UserMapper;
import ua.com.sipsoft.service.dto.request.RouteSheetDto;

@Mapper(uses = { CourierRequestMapper.class, HistoryEventMapper.class, UserMapper.class })
public interface RouteSheetMapper {

	RouteSheetMapper MAPPER = Mappers.getMapper(RouteSheetMapper.class);

	// DraftRouteSheet
	RouteSheetDto draftRouteSheetToDto(DraftRouteSheet sheet);

	DraftRouteSheet fromDtoToDraftRouteSheet(RouteSheetDto sheet);

	List<RouteSheetDto> draftRouteSheetToDto(List<DraftRouteSheet> request);

	List<DraftRouteSheet> fromDtoToDraftRouteSheet(List<RouteSheetDto> request);

	Stream<RouteSheetDto> draftRouteSheetToDto(Stream<DraftRouteSheet> request);

	Stream<DraftRouteSheet> fromDtoToDraftRouteSheet(Stream<RouteSheetDto> request);

	Set<RouteSheetDto> draftRouteSheetToDto(Set<DraftRouteSheet> request);

	Set<DraftRouteSheet> fromDtoToDraftRouteSheet(Set<RouteSheetDto> request);

	// IssuedRouteSheet
	RouteSheetDto issuedRouteSheetToDto(IssuedRouteSheet sheet);

	IssuedRouteSheet fromDtoToIssuedRouteSheet(RouteSheetDto sheet);

	List<RouteSheetDto> issuedRouteSheetToDto(List<IssuedRouteSheet> request);

	List<IssuedRouteSheet> fromDtoToIssuedRouteSheet(List<RouteSheetDto> request);

	Stream<RouteSheetDto> issuedRouteSheetToDto(Stream<IssuedRouteSheet> request);

	Stream<IssuedRouteSheet> fromDtoToIssuedRouteSheet(Stream<RouteSheetDto> request);

	Set<RouteSheetDto> issuedRouteSheetToDto(Set<IssuedRouteSheet> request);

	Set<IssuedRouteSheet> fromDtoToIssuedRouteSheet(Set<RouteSheetDto> request);

	// ArchivedRouteSheet
	RouteSheetDto archivedRouteSheetToDto(ArchivedRouteSheet sheet);

	ArchivedRouteSheet fromDtoToArchivedRouteSheet(RouteSheetDto sheet);

	List<RouteSheetDto> archivedRouteSheetToDto(List<ArchivedRouteSheet> request);

	List<ArchivedRouteSheet> fromDtoToArchivedRouteSheet(List<RouteSheetDto> request);

	Stream<RouteSheetDto> archivedRouteSheetToDto(Stream<ArchivedRouteSheet> request);

	Stream<ArchivedRouteSheet> fromDtoToArchivedRouteSheet(Stream<RouteSheetDto> request);

	Set<RouteSheetDto> archivedRouteSheetToDto(Set<ArchivedRouteSheet> request);

	Set<ArchivedRouteSheet> fromDtoToArchivedRouteSheet(Set<RouteSheetDto> request);

	// DraftRouteSheet to ArchivedRouteSheet
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "version", ignore = true)
	ArchivedRouteSheet draftRouteSheetToArchive(DraftRouteSheet sheet);

	List<ArchivedRouteSheet> draftRouteSheetToArchive(List<DraftRouteSheet> sheet);

	Stream<ArchivedRouteSheet> draftRouteSheetToArchive(Stream<DraftRouteSheet> sheet);

	Set<ArchivedRouteSheet> draftRouteSheetToArchive(Set<DraftRouteSheet> sheet);

	// IssuedRouteSheet to ArchivedRouteSheet
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "version", ignore = true)
	ArchivedRouteSheet issuedRouteSheetToArchive(IssuedRouteSheet sheet);

	List<ArchivedRouteSheet> issuedRouteSheetToArchive(List<IssuedRouteSheet> sheet);

	Stream<ArchivedRouteSheet> issuedRouteSheetToArchive(Stream<IssuedRouteSheet> sheet);

	Set<ArchivedRouteSheet> issuedRouteSheetToArchive(Set<IssuedRouteSheet> sheet);
}
