package ua.com.sipsoft.repository.serviceimpl.request.draft;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.request.archive.ArchivedRouteSheet;
import ua.com.sipsoft.dao.request.draft.CourierRequest;
import ua.com.sipsoft.dao.request.draft.DraftRouteSheet;
import ua.com.sipsoft.dao.request.issued.IssuedRouteSheet;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.repository.request.draft.DraftRouteSheetRepository;
import ua.com.sipsoft.repository.serviceimpl.mapper.request.RouteSheetMapper;
import ua.com.sipsoft.service.dto.request.DraftSheetUpdReqDto;
import ua.com.sipsoft.service.dto.request.RouteSheetDto;
import ua.com.sipsoft.service.request.archive.ArchivedSheetsService;
import ua.com.sipsoft.service.request.draft.CourierRequestService;
import ua.com.sipsoft.service.request.draft.DraftRouteSheetFilter;
import ua.com.sipsoft.service.request.draft.DraftRouteSheetServiceToRepo;
import ua.com.sipsoft.service.request.issued.IssuedRouteSheetService;
import ua.com.sipsoft.service.util.EntityFilter;
import ua.com.sipsoft.service.util.HasFilteredList;
import ua.com.sipsoft.service.util.HasLimitedList;
import ua.com.sipsoft.service.util.HasPagingRequestToSortConvertor;
import ua.com.sipsoft.service.util.HasQueryToSortConvertor;
import ua.com.sipsoft.util.paging.Page;
import ua.com.sipsoft.util.paging.PagingRequest;
import ua.com.sipsoft.util.query.Query;

/**
 * The Class DraftRouteSheetServiceImpl.
 *
 * @author Pavlo Degtyaryev
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DraftRouteSheetServiceImpl
		implements DraftRouteSheetServiceToRepo, HasQueryToSortConvertor, HasPagingRequestToSortConvertor,
		HasFilteredList, HasLimitedList {

	/** The dao. */
	private final DraftRouteSheetRepository dao;

	/** The archive route sheet service. */
	private final ArchivedSheetsService archiveRouteSheetService;

	/** The issued route sheet service. */
	private final IssuedRouteSheetService issuedRouteSheetService;

	/** The courier request service. */
	private final CourierRequestService courierRequestService;

	private final RouteSheetMapper routeSheetMapper;

	/**
	 * Clean draft sheets from courier requests.
	 *
	 * @param requests the requests
	 */
	@Override
	public void cleanDraftSheetsFromCourierRequests(Collection<CourierRequest> requests) {
		log.info("Clean Draft Route Sheet(s) from Courier Request(s)");
		if (requests == null || requests.isEmpty()) {
			log.warn("Missing some data or nothing delete. Cleaning impossible.");
			return;
		}
		List<DraftRouteSheet> draftRouteSheetList = dao.getByRequests(requests);
		draftRouteSheetList
				.forEach(sheet -> removeRequestsAndSave(sheet, requests));
	}

	/**
	 * Removes the requests and save.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @param requests        the requests
	 * @return the optional
	 */
	@Override
	public Optional<DraftRouteSheet> removeRequestsAndSave(DraftRouteSheet draftRouteSheet,
			Collection<CourierRequest> requests) {
		log.info("Remove Courier Request(s) from Draft Route Sheet");
		if (draftRouteSheet == null || requests == null) {
			log.warn("Missing some data. Remove impossible.");
			return Optional.empty();
		}
		boolean someRequestcCleaned = draftRouteSheet.getRequests().removeAll(requests);
		if (someRequestcCleaned) {
			return save(draftRouteSheet);
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Creates the and save draft route sheet.
	 *
	 * @param requests    the requests
	 * @param description the description
	 * @param author      the author
	 * @return the optional
	 */
	@Override
	public Optional<DraftRouteSheet> createAndSaveDraftRouteSheet(Collection<CourierRequest> requests,
			String description, User author) {
		log.info("Create and save Draft Route Sheet");
		if (requests == null || author == null) {
			log.warn("Missing some data. Save impossible.");
			Optional.empty();
		}
		DraftRouteSheet draftRouteSheet = new DraftRouteSheet();
		draftRouteSheet.setAuthor(author);
		draftRouteSheet.setDescription(description);
		if (!requests.isEmpty()) {
			draftRouteSheet.getRequests().addAll(requests);
		}
		// todo possible to make registration of added requests?
		draftRouteSheet.addHistoryEvent("Чернетку маршрутного листа було створено.", draftRouteSheet.getCreationDate(),
				author);
		return save(draftRouteSheet);
	}

	/**
	 * Save.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @return the optional
	 */
	@Override
	public Optional<DraftRouteSheet> save(DraftRouteSheet draftRouteSheet) {
		log.info("Save Draft Route Sheet: " + draftRouteSheet);
		if (draftRouteSheet == null) {
			log.warn("Missing some data. Save impossible.");
			return Optional.empty();
		}
		return Optional.of(dao.saveAndFlush(draftRouteSheet));
	}

	/**
	 * Move to archive.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @param movingReason    the moving reason
	 * @param author          the author
	 */
	@Override
	public void moveToArchive(DraftRouteSheet draftRouteSheet, String movingReason, User author) {
		log.info("Move to Archive Draft Route Sheet");
		if (draftRouteSheet == null || movingReason == null || author == null) {
			log.warn("Missing some data. Move is impossible.");
			return;
		}
		draftRouteSheet.getRequests().clear();
		ArchivedRouteSheet archivedRouteSheet = new ArchivedRouteSheet(draftRouteSheet);
		dao.delete(draftRouteSheet);
		archivedRouteSheet.addHistoryEvent(
				new StringBuilder()
						.append("Скасовано з причини '")
						.append(movingReason)
						.append("'. Переміщено до архіву.")
						.toString(),
				LocalDateTime.now(), author);
		archiveRouteSheetService.save(archivedRouteSheet);
	}

	/**
	 * Issue draft route sheet.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @param description     the description
	 * @param author          the author
	 */
	@Override
	public void issueDraftRouteSheet(DraftRouteSheet draftRouteSheet, String description, User author) {
		log.info("Do Issue Draft Route Sheet");
		if (draftRouteSheet == null || author == null || description == null) {
			log.warn("Missing some data. Issue is impossible.");
			return;
		}
		IssuedRouteSheet issuedRouteSheet = new IssuedRouteSheet(draftRouteSheet, author);
		issuedRouteSheet.addHistoryEvent(
				new StringBuilder()
						.append("Чернетку маршрутного листа було видано")
						.append(issuedRouteSheet.getDescription().equals(description) ? "."
								: (" з новим приписом \"").concat(description).concat("\"."))
						.toString(),
				LocalDateTime.now(), author);
		issuedRouteSheet.setDescription(description);
		issuedRouteSheet.setCreationDate(LocalDateTime.now());
		issuedRouteSheetService.save(issuedRouteSheet);

		cleanDraftSheetsFromCourierRequests(draftRouteSheet.getRequests());
		courierRequestService.delete(draftRouteSheet.getRequests());

		// draftRouteSheet.getRequests().clear();
		dao.deleteById(draftRouteSheet.getId());
		dao.flush();
		// dao.delete(draftRouteSheet);

	}

	/**
	 * Adds the courier requests and save.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @param author          the author
	 * @param request         the request
	 * @return the optional
	 */
	@Override
	public Optional<DraftRouteSheet> addCourierRequestsAndSave(DraftRouteSheet draftRouteSheet, User author,
			CourierRequest... request) {
		log.info("Add Courier Request(s) to Draft Route Sheet");
		if (draftRouteSheet == null || request == null || author == null || (request != null && request.length < 1)) {
			log.warn("Missing some data. Save impossible.");
			return Optional.empty();
		}
		for (CourierRequest courierRequest : request) {
			draftRouteSheet.getRequests().add(courierRequest);
		}
		try {
			draftRouteSheet.addHistoryEvent(request.length + " request(s) was added.", LocalDateTime.now(), author);
		} catch (Exception e) {
			log.error("Error on add Corier Request(s) to the Draft Route Sheet for a reason: {}", e.getMessage());
		}
		return save(draftRouteSheet);
	}

	/**
	 * Adds the requests and save.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @param requests        the requests
	 * @param author          the author
	 * @return the optional
	 */
	@Override
	public Optional<DraftRouteSheet> addRequestsAndSave(DraftRouteSheet draftRouteSheet,
			Collection<CourierRequest> requests,
			User author) {
		log.info("Add Courier Request(s) to Draft Route Sheet");
		if (draftRouteSheet == null || requests == null || author == null) {
			log.warn("Missing some data. Add impossible.");
			return Optional.empty();
		}
		draftRouteSheet.getRequests().addAll(requests);
		return save(draftRouteSheet);
	}

	/**
	 * Fetch by id.
	 *
	 * @param id the id
	 * @return the optional
	 */
	@Override
	public Optional<RouteSheetDto> fetchById(@NonNull Long id) {
		log.debug("Get Route Sheet id: '{}'", id);
		RouteSheetDto sheet = null;
		try {
			Optional<DraftRouteSheet> routeSheet = dao.findById(id);
			if (routeSheet.isPresent()) {
				sheet = routeSheetMapper.draftRouteSheetToDto(routeSheet.get());
			}
		} catch (Exception e) {
			log.error("The Route Sheet bi id is not received for a reason: {}", e.getMessage());
		}
		return Optional.ofNullable(sheet);
	}

	/**
	 * Checks if is entity pass filter.
	 *
	 * @param entity the entity
	 * @param filter the filter
	 * @return true, if is entity pass filter
	 */
	private boolean isEntityPassFilter(DraftRouteSheet entity, DraftRouteSheetFilter filter) {
		return entity.getDescription()
				.toLowerCase()
				.contains(filter.getDescription() == null ? "" : filter.getDescription().toLowerCase());
	}

	/**
	 * Gets the queried draft route sheets.
	 *
	 * @param query the query
	 * @return the queried draft route sheets
	 */
	@Override
	public Stream<DraftRouteSheet> getQueriedDraftRouteSheets(
			Query<DraftRouteSheet, EntityFilter<DraftRouteSheet>> query) {
		log.debug("Get requested page Drafr Route Sheets with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
				query.getOffset(), query.getLimit(), query.getSortOrders(), query.getFilter().get().toString());
		if (query == null || query.getFilter().isEmpty()) {
			log.debug("Get Drafr Route Sheets is impossible. Miss some data.");
			return Stream.empty();
		}
		try {
			return dao.findAll(queryToSort(query))
					.stream()
					.filter(entity -> query.getFilter().get().isPass(entity))
					.skip(query.getOffset())
					.limit(query.getLimit());
		} catch (Exception e) {
			log.error("The Drafr Route Sheets list was not received for a reason: {}", e.getMessage());
		}
		return Stream.empty();
	}

	/**
	 * Gets the queried draft route sheets count.
	 *
	 * @param query the query
	 * @return the queried draft route sheets count
	 */
	@Override
	public int getQueriedDraftRouteSheetsCount(Query<DraftRouteSheet, EntityFilter<DraftRouteSheet>> query) {
		log.debug("Get requested size Drafr Route Sheets  with filter '{}'", query.getFilter().get().toString());
		return (int) getQueriedDraftRouteSheets(query).count();
	}

	@Override
	public Page<RouteSheetDto> getFilteredPage(@NonNull PagingRequest pagingRequest,
			@NonNull EntityFilter<RouteSheetDto> entityFilter) {
		log.debug(
				"getFilteredPage] - Get requested page Draft Sheet with PagingRequest '{}' and EntityFilter<Facility> '{}'",
				pagingRequest, entityFilter);

		Page<RouteSheetDto> page = new Page<>();
		List<DraftRouteSheet> routeSheets;
		routeSheets = dao.findAll(toSort(pagingRequest));

		List<RouteSheetDto> routeSheetsDto = getFiteredList(routeSheetMapper.draftRouteSheetToDto(routeSheets),
				entityFilter);

		page.setRecordsTotal(routeSheetsDto.size());

		page.setRecordsFiltered(routeSheetsDto.size());

		routeSheetsDto = getLimitedList(routeSheetsDto, pagingRequest.getStart(), pagingRequest.getLength());

		page.setData(routeSheetsDto);

		page.setDraw(pagingRequest.getDraw());

		return page;
	}

	@Override
	public Optional<RouteSheetDto> save(@NonNull RouteSheetDto request) {
		log.debug(
				"save] - Save Draft Sheet '{}'", request);
		DraftRouteSheet routeSheet = routeSheetMapper.fromDtoToDraftRouteSheet(request);
		routeSheet = dao.save(routeSheet);
		return Optional.ofNullable(routeSheetMapper.draftRouteSheetToDto(routeSheet));
	}

	@Override
	public Optional<RouteSheetDto> updateRouteSheet(@NonNull DraftSheetUpdReqDto draftSheetUpdReqDto) {
		// TODO Auto-generated method stub
		return null;
	}
}