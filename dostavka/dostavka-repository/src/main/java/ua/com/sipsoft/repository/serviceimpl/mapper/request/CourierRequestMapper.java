package ua.com.sipsoft.repository.serviceimpl.mapper.request;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.dao.request.archive.ArchivedCourierVisit;
import ua.com.sipsoft.dao.request.draft.CourierRequest;
import ua.com.sipsoft.dao.request.issued.CourierVisit;
import ua.com.sipsoft.repository.serviceimpl.mapper.facility.FacilityAddMapper;
import ua.com.sipsoft.repository.serviceimpl.mapper.security.UserMapper;
import ua.com.sipsoft.service.dto.request.CourierRequestDto;

@Mapper(componentModel = "spring", uses = { FacilityAddMapper.class, HistoryEventMapper.class, UserMapper.class })
@Component
public interface CourierRequestMapper {

	// CourierRequest
	CourierRequestDto courierRequestToDto(CourierRequest request);

	CourierRequest fromDtoToCourierRequest(CourierRequestDto request);

	List<CourierRequestDto> courierRequestToDto(List<CourierRequest> request);

	List<CourierRequest> fromDtoToCourierRequest(List<CourierRequestDto> request);

	Stream<CourierRequestDto> courierRequestToDto(Stream<CourierRequest> request);

	Stream<CourierRequest> fromDtoToCourierRequest(Stream<CourierRequestDto> request);

	Set<CourierRequestDto> courierRequestToDto(Set<CourierRequest> request);

	Set<CourierRequest> fromDtoToCourierRequest(Set<CourierRequestDto> request);

	// CourierVisit
	CourierRequestDto courierVisitToDto(CourierVisit request);

	CourierVisit fromDtoToCourierVisit(CourierRequestDto request);

	List<CourierRequestDto> courierVisitToDto(List<CourierVisit> request);

	List<CourierVisit> fromDtoToCourierVisit(List<CourierRequestDto> request);

	Stream<CourierRequestDto> courierVisitToDto(Stream<CourierVisit> request);

	Stream<CourierVisit> fromDtoToCourierVisit(Stream<CourierRequestDto> request);

	Set<CourierRequestDto> courierVisitToDto(Set<CourierVisit> request);

	Set<CourierVisit> fromDtoToCourierVisit(Set<CourierRequestDto> request);

	// ArchivedCourierVisit
	CourierRequestDto archivedCourierVisitToDto(ArchivedCourierVisit request);

	ArchivedCourierVisit fromDtoToArchivedCourierVisit(CourierRequestDto request);

	List<CourierRequestDto> archivedCourierVisitToDto(List<ArchivedCourierVisit> request);

	List<ArchivedCourierVisit> fromDtoToArchivedCourierVisit(List<CourierRequestDto> request);

	Stream<CourierRequestDto> archivedCourierVisitToDto(Stream<ArchivedCourierVisit> request);

	Stream<ArchivedCourierVisit> fromDtoToArchivedCourierVisit(Stream<CourierRequestDto> request);

	Set<CourierRequestDto> archivedCourierVisitToDto(Set<ArchivedCourierVisit> request);

	Set<ArchivedCourierVisit> fromDtoToArchivedCourierVisit(Set<CourierRequestDto> request);

	// Direct CourierRequest to from ArchivedCourierVisit
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "version", ignore = true)
	ArchivedCourierVisit courierRequestToArchive(CourierRequest request);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "version", ignore = true)
	CourierRequest courierRequestToArchive(ArchivedCourierVisit request);

	List<ArchivedCourierVisit> courierRequestToArchive(List<CourierRequest> request);

	List<CourierRequest> archiveToCourierRequest(List<ArchivedCourierVisit> request);

	Stream<ArchivedCourierVisit> courierRequestToArchive(Stream<CourierRequest> request);

	Stream<CourierRequest> archiveToCourierRequest(Stream<ArchivedCourierVisit> request);

	Set<ArchivedCourierVisit> courierRequestToArchive(Set<CourierRequest> request);

	Set<CourierRequest> archiveToCourierRequest(Set<ArchivedCourierVisit> request);

	// Direct CourierVisit to ArchivedCourierVisit
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "version", ignore = true)
	ArchivedCourierVisit courierRequestToArchive(CourierVisit request);

	List<ArchivedCourierVisit> courierVisitToArchive(List<CourierVisit> request);

	Stream<ArchivedCourierVisit> courierVisitToArchive(Stream<CourierVisit> request);

	Set<ArchivedCourierVisit> courierVisitToArchive(Set<CourierVisit> request);

}
