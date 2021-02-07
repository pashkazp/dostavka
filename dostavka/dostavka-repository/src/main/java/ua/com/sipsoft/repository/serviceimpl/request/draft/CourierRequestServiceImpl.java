package ua.com.sipsoft.repository.serviceimpl.request.draft;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.request.archive.ArchivedCourierVisit;
import ua.com.sipsoft.dao.request.draft.CourierRequest;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.dao.util.CourierRequestSnapshot;
import ua.com.sipsoft.repository.request.draft.CourierRequestRepository;
import ua.com.sipsoft.service.request.archive.ArchivedVisitsService;
import ua.com.sipsoft.service.request.draft.CourierRequestService;
import ua.com.sipsoft.service.request.draft.DraftRouteSheetService;
import ua.com.sipsoft.service.security.UsersUtils;
import ua.com.sipsoft.service.util.EntityFilter;
import ua.com.sipsoft.service.util.HasQueryToSortConvertor;
import ua.com.sipsoft.util.CourierVisitState;
import ua.com.sipsoft.util.query.Query;

/**
 * The Class CourierRequestServiceImpl.
 *
 * @author Pavlo Degtyaryev
 */
@Slf4j
@Service
@Transactional
public class CourierRequestServiceImpl implements CourierRequestService, HasQueryToSortConvertor {

	/** The dao. */
	private final CourierRequestRepository dao;

	/** The draft route sheet service. */
	private final DraftRouteSheetService draftRouteSheetService;

	/** The archived courier visit service. */
	private final ArchivedVisitsService archivedCourierVisitService;

	/**
	 * @param dao
	 * @param draftRouteSheetService
	 * @param archivedCourierVisitService
	 */
	public CourierRequestServiceImpl(@Lazy CourierRequestRepository dao,
			@Lazy DraftRouteSheetService draftRouteSheetService,
			@Lazy ArchivedVisitsService archivedCourierVisitService) {
		super();
		this.dao = dao;
		this.draftRouteSheetService = draftRouteSheetService;
		this.archivedCourierVisitService = archivedCourierVisitService;
	}

	/**
	 * Register changes and save.
	 *
	 * @param request         the request
	 * @param requestSnapshot the request snapshot
	 * @param author          the author
	 * @return the optional
	 */
	@Override
	public Optional<CourierRequest> registerChangesAndSave(CourierRequest request,
			CourierRequestSnapshot requestSnapshot, User author) {
		log.info("Save courier Request: " + request);
		if (request == null || requestSnapshot == null) {
			log.warn("Save Courier Request impossible. Some data missing: {}", request);
			return Optional.empty();
		}
		requestSnapshot.fillHistoryChangesTo(request, UsersUtils.getCurrentUser());
		return save(request);
	}

	/**
	 * Adds the request.
	 *
	 * @param request the request
	 * @param author  the author
	 * @return the optional
	 */
	@Override
	public Optional<CourierRequest> addRequest(CourierRequest request, User author) {
		log.info("Add Courier Request: " + request);
		if (request == null) {
			log.warn("Save Courier Request impossible. Some data missing: {}", request);
			return Optional.empty();
		}
		request.setCreationDate(LocalDateTime.now());
		request.addHistoryEvent("Зареестровано виклик курьера.", LocalDateTime.now(),
				UsersUtils.getCurrentUser());
		return save(request);
	}

	/**
	 * Save.
	 *
	 * @param request the request
	 * @return the optional
	 */
	@Override
	public Optional<CourierRequest> save(CourierRequest request) {
		log.info("Save courier Request: " + request);
		if (request == null) {
			log.warn("Save Courier Request impossible. Some data missing: {}", request);
			return Optional.empty();
		}
		return Optional.ofNullable(dao.saveAndFlush(request));
	}

	/**
	 * Move courier requests to archive.
	 *
	 * @param courierRequestsList the courier requests list
	 * @param movingReason        the moving reason
	 * @param author              the author
	 */
	@Override
	public void moveCourierRequestsToArchive(List<CourierRequest> courierRequestsList, String movingReason,
			User author) {
		log.info("Move Courier Request(s) to archive");
		if (courierRequestsList == null || movingReason == null || author == null) {
			log.warn("Move Courier Request(s) is impossible. Some data missing");
			return;
		}

		draftRouteSheetService.cleanDraftSheetsFromCourierRequests(courierRequestsList);

		for (CourierRequest courierRequest : courierRequestsList) {
			Optional<CourierRequest> persistedRequest = dao.findById(courierRequest.getId());
			if (persistedRequest.isPresent()) {
				ArchivedCourierVisit archivedRequest = new ArchivedCourierVisit(persistedRequest.get());
				archivedRequest.setState(CourierVisitState.CANCELLED);
				archivedRequest.addHistoryEvent(
						new StringBuilder()
								.append("Скасовано з причини \"")
								.append(movingReason)
								.append("\". Переміщено до архіву.")
								.toString(),
						LocalDateTime.now(), author);
				archivedCourierVisitService.save(archivedRequest);
				dao.deleteById(persistedRequest.get().getId());
			}
		}
	}

	/**
	 * Delete.
	 *
	 * @param courierRequest the courier request
	 */
	@Override
	public void delete(Collection<CourierRequest> courierRequest) {
		log.info("Delete Courier Request(s)");
		if (courierRequest == null || courierRequest.isEmpty()) {
			log.warn("Delete Courier Request(s) is impossible or nothing to delete. Some data missing");
		} else {
			dao.deleteAll(courierRequest);
		}
	}

	/**
	 * Fetch by id.
	 *
	 * @param id the id
	 * @return the optional
	 */
	@Override
	public Optional<CourierRequest> fetchById(Long id) {
		log.debug("Get Courier Requests by id: '{}'", id);
		if (id == null) {
			log.debug("Get Courier Requests by id is impossible. id is null.");
			return Optional.empty();
		}
		try {
			return dao.findById(id);
		} catch (Exception e) {
			log.error("The Courier Requests by id is not received for a reason: {}", e.getMessage());
			return Optional.empty();
		}
	}

	/**
	 * Gets the queried courier requests by filter.
	 *
	 * @param query the query
	 * @return the queried courier requests by filter
	 */
	@Override
	public Stream<CourierRequest> getQueriedCourierRequestsByFilter(
			Query<CourierRequest, EntityFilter<CourierRequest>> query) {
		log.debug(
				"Get requested page Courier Requests By Sheet ID with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
				query.getOffset(), query.getLimit(), query.getSortOrders(), query.getFilter().get().toString());
		if (query == null || query.getFilter().isEmpty()) {
			log.debug("Get Courier Requests Sheets is impossible. Miss some data.");
			return Stream.empty();
		}
		try {
			return dao.findAll(queryToSort(query))
					.stream()
					.filter(entity -> query.getFilter().get().isPass(entity))
					.skip(query.getOffset())
					.limit(query.getLimit());
		} catch (Exception e) {
			log.error("The Courier Requests list was not received for a reason: {}", e.getMessage());
		}
		return Stream.empty();
	}

	/**
	 * Gets the queried courier requests by filter count.
	 *
	 * @param query the query
	 * @return the queried courier requests by filter count
	 */
	@Override
	public int getQueriedCourierRequestsByFilterCount(Query<CourierRequest, EntityFilter<CourierRequest>> query) {
		log.debug("Get requested size Courier Requests with filter '{}'", query.getFilter().get().toString());
		return (int) getQueriedCourierRequestsByFilter(query).count();
	}

	/**
	 * Gets the queried courier requests by filter.
	 *
	 * @param query   the query
	 * @param SheetId the sheet id
	 * @return the queried courier requests by filter
	 */
	@Override
	public Stream<CourierRequest> getQueriedCourierRequestsByFilterBySheetId(
			Query<CourierRequest, EntityFilter<CourierRequest>> query, Long SheetId) {
		log.debug(
				"Get requested page Courier Requests By Sheet ID with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
				query.getOffset(), query.getLimit(), query.getSortOrders(), query.getFilter().get().toString());
		if (query == null || query.getFilter().isEmpty()) {
			log.debug("Get Courier Requests Sheets is impossible. Miss some data.");
			return Stream.empty();
		}
		try {
			return dao.getCourierRequestByDratRequestSheetId(SheetId, queryToSort(query))
					.stream()
					.filter(entity -> query.getFilter().get().isPass(entity))
					.skip(query.getOffset())
					.limit(query.getLimit());
		} catch (Exception e) {
			log.error("The Courier Requests list was not received for a reason: {}", e.getMessage());
		}
		return Stream.empty();
	}

	/**
	 * Gets the queried courier requests by filter count.
	 *
	 * @param query   the query
	 * @param SheetId the sheet id
	 * @return the queried courier requests by filter count
	 */
	@Override
	public int getQueriedCourierRequestsByFilterBySheetIdCount(
			Query<CourierRequest, EntityFilter<CourierRequest>> query, Long SheetId) {
		log.debug("Get requested size Courier Requests with filter '{}'", query.getFilter().get().toString());
		return (int) getQueriedCourierRequestsByFilterBySheetId(query, SheetId).count();
	}

}