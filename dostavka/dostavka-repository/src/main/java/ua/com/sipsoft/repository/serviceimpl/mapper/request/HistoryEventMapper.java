package ua.com.sipsoft.repository.serviceimpl.mapper.request;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.dao.request.archive.ArchivedCourierVisitEvent;
import ua.com.sipsoft.dao.request.archive.ArchivedRouteSheetEvent;
import ua.com.sipsoft.dao.request.draft.CourierRequestEvent;
import ua.com.sipsoft.dao.request.draft.DraftRouteSheetEvent;
import ua.com.sipsoft.dao.request.issued.CourierVisitEvent;
import ua.com.sipsoft.dao.request.issued.IssuedRouteSheetEvent;
import ua.com.sipsoft.repository.serviceimpl.mapper.security.UserMapper;
import ua.com.sipsoft.service.dto.request.HistoryEventDto;

@Mapper(componentModel = "spring", uses = UserMapper.class)
@Component
public interface HistoryEventMapper {

	// CourierVisitEvent
	HistoryEventDto courierVisitEventToDto(CourierVisitEvent historyEvent);

	CourierVisitEvent fromDtoToCourierVisitEvent(HistoryEventDto historyEvent);

	List<HistoryEventDto> courierVisitEventToDto(List<CourierVisitEvent> historyEvent);

	List<CourierVisitEvent> fromDtoToCourierVisitEvent(List<HistoryEventDto> historyEvent);

	Stream<HistoryEventDto> courierVisitEventToDto(Stream<CourierVisitEvent> historyEvent);

	Stream<CourierVisitEvent> fromDtoToCourierVisitEvent(Stream<HistoryEventDto> historyEvent);

	Set<HistoryEventDto> courierVisitEventToDto(Set<CourierVisitEvent> historyEvent);

	Set<CourierVisitEvent> fromDtoToCourierVisitEvent(Set<HistoryEventDto> historyEvent);

	// ArchivedCourierVisitEvent
	HistoryEventDto archivedCourierVisitEventToDto(ArchivedCourierVisitEvent historyEvent);

	ArchivedCourierVisitEvent fromDtoToArchivedCourierVisitEvent(HistoryEventDto historyEvent);

	List<HistoryEventDto> archivedCourierVisitEventToDto(List<ArchivedCourierVisitEvent> historyEvent);

	List<ArchivedCourierVisitEvent> fromDtoToArchivedCourierVisitEvent(
			List<HistoryEventDto> historyEvent);

	Stream<HistoryEventDto> archivedCourierVisitEventToDto(
			Stream<ArchivedCourierVisitEvent> historyEvent);

	Stream<ArchivedCourierVisitEvent> fromDtoToArchivedCourierVisitEvent(
			Stream<HistoryEventDto> historyEvent);

	Set<HistoryEventDto> archivedCourierVisitEventToDto(Set<ArchivedCourierVisitEvent> historyEvent);

	Set<ArchivedCourierVisitEvent> fromDtoToArchivedCourierVisitEvent(
			Set<HistoryEventDto> historyEvent);

	// CourierRequestEvent
	HistoryEventDto courierRequestEventToDto(CourierRequestEvent historyEvent);

	CourierRequestEvent fromDtoToCourierRequestEvent(HistoryEventDto historyEvent);

	List<HistoryEventDto> courierRequestEventToDto(List<CourierRequestEvent> historyEvent);

	List<CourierRequestEvent> fromDtoToCourierRequestEvent(List<HistoryEventDto> historyEvent);

	Stream<HistoryEventDto> courierRequestEventToDto(Stream<CourierRequestEvent> historyEvent);

	Stream<CourierRequestEvent> fromDtoToCourierRequestEvent(Stream<HistoryEventDto> historyEvent);

	Set<HistoryEventDto> courierRequestEventToDto(Set<CourierRequestEvent> historyEvent);

	Set<CourierRequestEvent> fromDtoToCourierRequestEvent(Set<HistoryEventDto> historyEvent);

	// ArchivedRouteSheetEvent
	HistoryEventDto archivedRouteSheetEventToDto(ArchivedRouteSheetEvent historyEvent);

	ArchivedRouteSheetEvent fromDtoToArchivedRouteSheetEvent(HistoryEventDto historyEvent);

	List<HistoryEventDto> archivedRouteSheetEventToDto(List<ArchivedRouteSheetEvent> historyEvent);

	List<ArchivedRouteSheetEvent> fromDtoToArchivedRouteSheetEvent(List<HistoryEventDto> historyEvent);

	Stream<HistoryEventDto> archivedRouteSheetEventToDto(Stream<ArchivedRouteSheetEvent> historyEvent);

	Stream<ArchivedRouteSheetEvent> fromDtoToArchivedRouteSheetEvent(
			Stream<HistoryEventDto> historyEvent);

	Set<HistoryEventDto> archivedRouteSheetEventToDto(Set<ArchivedRouteSheetEvent> historyEvent);

	Set<ArchivedRouteSheetEvent> fromDtoToArchivedRouteSheetEvent(Set<HistoryEventDto> historyEvent);

	// DraftRouteSheetEvent
	HistoryEventDto draftRouteSheetEventToDto(DraftRouteSheetEvent historyEvent);

	DraftRouteSheetEvent fromDtoToDraftRouteSheetEvent(HistoryEventDto historyEvent);

	List<HistoryEventDto> draftRouteSheetEventToDto(List<DraftRouteSheetEvent> historyEvent);

	List<DraftRouteSheetEvent> fromDtoToDraftRouteSheetEvent(List<HistoryEventDto> historyEvent);

	Stream<HistoryEventDto> draftRouteSheetEventToDto(Stream<DraftRouteSheetEvent> historyEvent);

	Stream<DraftRouteSheetEvent> fromDtoToDraftRouteSheetEvent(Stream<HistoryEventDto> historyEvent);

	Set<HistoryEventDto> draftRouteSheetEventToDto(Set<DraftRouteSheetEvent> historyEvent);

	Set<DraftRouteSheetEvent> fromDtoToDraftRouteSheetEvent(Set<HistoryEventDto> historyEvent);

	// IssuedRouteSheetEvent
	HistoryEventDto issuedRouteSheetEventToDto(IssuedRouteSheetEvent historyEvent);

	IssuedRouteSheetEvent fromDtoToIssuedRouteSheetEvent(HistoryEventDto historyEvent);

	List<HistoryEventDto> issuedRouteSheetEventToDto(List<IssuedRouteSheetEvent> historyEvent);

	List<IssuedRouteSheetEvent> fromDtoToIssuedRouteSheetEvent(List<HistoryEventDto> historyEvent);

	Stream<HistoryEventDto> issuedRouteSheetEventToDto(Stream<IssuedRouteSheetEvent> historyEvent);

	Stream<IssuedRouteSheetEvent> fromDtoToIssuedRouteSheetEvent(Stream<HistoryEventDto> historyEvent);

	Set<HistoryEventDto> issuedRouteSheetEventToDto(Set<IssuedRouteSheetEvent> historyEvent);

	Set<IssuedRouteSheetEvent> fromDtoToIssuedRouteSheetEvent(Set<HistoryEventDto> historyEvent);

	// Direct CourierRequestEvent to from ArchivedCourierVisitEvent
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "version", ignore = true)
	ArchivedCourierVisitEvent courierRequestEventToArchive(CourierRequestEvent historyEvent);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "version", ignore = true)
	CourierRequestEvent archivedToCourierRequestEvent(ArchivedCourierVisitEvent historyEvent);

	List<ArchivedCourierVisitEvent> courierRequestEventToArchive(List<CourierRequestEvent> historyEvent);

	List<CourierRequestEvent> archivedToCourierRequestEvent(List<ArchivedCourierVisitEvent> historyEvent);

	Stream<ArchivedCourierVisitEvent> courierRequestEventToArchive(Stream<CourierRequestEvent> historyEvent);

	Stream<CourierRequestEvent> archivedToCourierRequestEvent(Stream<ArchivedCourierVisitEvent> historyEvent);

	Set<ArchivedCourierVisitEvent> courierRequestEventToArchive(Set<CourierRequestEvent> historyEvent);

	Set<CourierRequestEvent> archivedToCourierRequestEvent(Set<ArchivedCourierVisitEvent> historyEvent);

	// Direct CourierVisitEvent to ArchivedCourierVisitEvent
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "version", ignore = true)
	ArchivedCourierVisitEvent courierVisitEventToArchive(CourierVisitEvent historyEvent);

	List<ArchivedCourierVisitEvent> courierVisitEventToArchive(List<CourierVisitEvent> historyEvent);

	Stream<ArchivedCourierVisitEvent> courierVisitEventToArchive(Stream<CourierVisitEvent> historyEvent);

	Set<ArchivedCourierVisitEvent> courierVisitEventToArchive(Set<CourierVisitEvent> historyEvent);

	// Direct IssuedRouteSheetEvent to ArchivedRouteSheetEvent
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "version", ignore = true)
	ArchivedRouteSheetEvent issuedRouteSheetEventToArchive(IssuedRouteSheetEvent historyEvent);

	List<ArchivedRouteSheetEvent> issuedRouteSheetEventToArchive(List<IssuedRouteSheetEvent> historyEvent);

	Stream<ArchivedRouteSheetEvent> issuedRouteSheetEventToArchive(Stream<IssuedRouteSheetEvent> historyEvent);

	Set<ArchivedRouteSheetEvent> issuedRouteSheetEventToArchive(Set<IssuedRouteSheetEvent> historyEvent);

	// Direct DraftRouteSheetEvent to ArchivedRouteSheetEvent
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "version", ignore = true)
	ArchivedRouteSheetEvent draftRouteSheetEventToArchive(DraftRouteSheetEvent historyEvent);

	List<ArchivedRouteSheetEvent> draftRouteSheetEventToArchive(List<DraftRouteSheetEvent> historyEvent);

	Stream<ArchivedRouteSheetEvent> draftRouteSheetEventToArchive(Stream<DraftRouteSheetEvent> historyEvent);

	Set<ArchivedRouteSheetEvent> draftRouteSheetEventToArchive(Set<DraftRouteSheetEvent> historyEvent);

}
