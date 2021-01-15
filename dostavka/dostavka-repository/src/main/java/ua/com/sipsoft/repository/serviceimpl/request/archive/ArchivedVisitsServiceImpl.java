package ua.com.sipsoft.repository.serviceimpl.request.archive;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.request.archive.ArchivedCourierVisit;
import ua.com.sipsoft.dao.request.draft.CourierRequest;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.repository.request.archive.ArchivedCourierVisitRepository;
import ua.com.sipsoft.service.request.archive.ArchivedVisitsService;
import ua.com.sipsoft.service.request.draft.CourierRequestService;
import ua.com.sipsoft.service.util.EntityFilter;
import ua.com.sipsoft.service.util.HasQueryToSortConvertor;
import ua.com.sipsoft.util.query.Query;

/**
 * The Interface ArchivedVisitsServiceImpl.
 *
 * @author Pavlo Degtyaryev
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ArchivedVisitsServiceImpl implements ArchivedVisitsService, HasQueryToSortConvertor {

	/** The dao. */
	private final ArchivedCourierVisitRepository dao;

	/** The courier request service. */
	private final CourierRequestService courierRequestService;

	/**
	 * Save.
	 *
	 * @param acv the acv
	 * @return the optional
	 */
	@Override
	public Optional<ArchivedCourierVisit> save(ArchivedCourierVisit acv) {
		log.info("Save courier Archived Courier Visit: " + acv);
		if (acv == null) {
			log.warn("Save Archive Courier Visit impossible. Some data missing");
			return Optional.empty();
		}
		return Optional.of(dao.saveAndFlush(acv));
	}

	/**
	 * Gets the queried courier visits by filter.
	 *
	 * @param query the query
	 * @return the queried courier visits by filter
	 */
	@Override
	public Stream<ArchivedCourierVisit> getQueriedCourierVisitsByFilter(
			Query<ArchivedCourierVisit, EntityFilter<ArchivedCourierVisit>> query) {
		log.debug(
				"Get requested page Archived Courier Visits By Sheet ID with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
				query.getOffset(), query.getLimit(), query.getSortOrders(), query.getFilter().get().toString());
		if (query == null || query.getFilter().isEmpty()) {
			log.debug("Get ourier Visits is impossible. Miss some data.");
			return Stream.empty();
		}
		try {
			return dao.findAll(queryToSort(query))
					.stream()
					.filter(entity -> query.getFilter().get().isPass(entity))
					.skip(query.getOffset())
					.limit(query.getLimit());
		} catch (Exception e) {
			log.error("The Archived Visits list was not received for a reason: {}", e.getMessage());
		}
		return Stream.empty();
	}

	/**
	 * Gets the queried courier visits by filter count.
	 *
	 * @param query the query
	 * @return the queried courier visits by filter count
	 */
	@Override
	public int getQueriedCourierVisitsByFilterCount(
			Query<ArchivedCourierVisit, EntityFilter<ArchivedCourierVisit>> query) {
		log.debug("Get requested size Archived Courier Visits with filter '{}'", query.getFilter().get().toString());
		return (int) getQueriedCourierVisitsByFilter(query).count();
	}

	/**
	 * Gets the queried courier visits by filter.
	 *
	 * @param query   the query
	 * @param sheetId the sheet id
	 * @return the queried courier visits by filter
	 */
	@Override
	public Stream<ArchivedCourierVisit> getQueriedCourierVisitsByFilterBySheetId(
			Query<ArchivedCourierVisit, EntityFilter<ArchivedCourierVisit>> query, Long sheetId) {
		log.debug(
				"Get requested page Archived Courier Visits By Sheet ID with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
				query.getOffset(), query.getLimit(), query.getSortOrders(), query.getFilter().get().toString());
		if (query == null || query.getFilter().isEmpty()) {
			log.debug("Get ourier Visits is impossible. Miss some data.");
			return Stream.empty();
		}
		try {
			return dao.getCourierVisitByArchivedSheetId(sheetId, queryToSort(query))
					.stream()
					.filter(entity -> query.getFilter().get().isPass(entity))
					.skip(query.getOffset())
					.limit(query.getLimit());
		} catch (Exception e) {
			log.error("The Archived Visits list was not received for a reason: {}", e.getMessage());
		}
		return Stream.empty();
	}

	/**
	 * Gets the queried courier visits by filter by sheet id count.
	 *
	 * @param query   the query
	 * @param sheetId the sheet id
	 * @return the queried courier visits by filter by sheet id count
	 */
	@Override
	public int getQueriedCourierVisitsByFilterBySheetIdCount(
			Query<ArchivedCourierVisit, EntityFilter<ArchivedCourierVisit>> query,
			Long sheetId) {
		log.debug("Get requested size Archived Courier Visits with filter '{}'", query.getFilter().get().toString());
		return (int) getQueriedCourierVisitsByFilterBySheetId(query, sheetId).count();
	}

	/**
	 * Redraft visits.
	 *
	 * @param visits      the visits
	 * @param description the description
	 * @param author      the author
	 */
	@Override
	public void redraftVisits(Collection<ArchivedCourierVisit> visits, String description, User author) {
		log.info("Redraft Archived Courier Visits");
		if (visits == null || description == null || author == null) {
			log.warn("Redraft Archived Courier Visits is impossible. Some data missing");
			return;
		}

		for (ArchivedCourierVisit courierVisit : visits) {
			Optional<ArchivedCourierVisit> persistedVisit = dao.findById(courierVisit.getId());
			if (persistedVisit.isPresent()) {
				CourierRequest courierRequest = new CourierRequest(persistedVisit.get(), author);
				courierRequest.addHistoryEvent(
						new StringBuilder()
								.append("Цей виклик було перестворено з архівного виклику з причини: \"")
								.append(description)
								.append("\". Прототип має #id ")
								.append(persistedVisit.get().getId().toString())
								.toString(),
						LocalDateTime.now(), author);
				Optional<CourierRequest> newCourierRequest = courierRequestService.addRequest(courierRequest, author);
				if (newCourierRequest.isPresent()) {
					persistedVisit.get().addHistoryEvent(
							new StringBuilder()
									.append("Цей виклик було взято за основу для повторного виклику з причини: \"")
									.append(description)
									.append("\". Нова чернетка зареєстрована з #id: ")
									.append(newCourierRequest.get().getId().toString())
									.toString(),
							LocalDateTime.now(), author);
					dao.saveAndFlush(persistedVisit.get());
				}
			}
		}
	}

}