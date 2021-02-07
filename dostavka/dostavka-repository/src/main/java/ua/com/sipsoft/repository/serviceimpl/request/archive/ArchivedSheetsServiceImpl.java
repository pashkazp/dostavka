package ua.com.sipsoft.repository.serviceimpl.request.archive;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.request.archive.ArchivedRouteSheet;
import ua.com.sipsoft.repository.request.archive.ArchivedRouteSheetRepository;
import ua.com.sipsoft.service.request.archive.ArchivedSheetsService;
import ua.com.sipsoft.service.util.EntityFilter;
import ua.com.sipsoft.service.util.HasQueryToSortConvertor;
import ua.com.sipsoft.util.query.Query;

/**
 * The Interface ArchivedSheetsServiceImpl.
 *
 * @author Pavlo Degtyaryev
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ArchivedSheetsServiceImpl implements ArchivedSheetsService, HasQueryToSortConvertor {

	/** The dao. */
	private final ArchivedRouteSheetRepository dao;

	/**
	 * Save.
	 *
	 * @param archivedRouteSheet the archived route sheet
	 * @return the optional
	 */
	@Override
	public Optional<ArchivedRouteSheet> save(ArchivedRouteSheet archivedRouteSheet) {
		log.info("Save Archived Route Sheet");
		if (archivedRouteSheet == null) {
			log.warn("Save Archived Route Sheet impossible. Some data missing");
			return Optional.empty();
		}
		return Optional.ofNullable(dao.saveAndFlush(archivedRouteSheet));
	}

	/**
	 * Gets the queried archived sheets by filter.
	 *
	 * @param query the query
	 * @return the queried archived sheets by filter
	 */
	@Override
	public Stream<ArchivedRouteSheet> getQueriedArchivedSheetsByFilter(
			Query<ArchivedRouteSheet, EntityFilter<ArchivedRouteSheet>> query) {
		log.debug("Get requested page Archived Route Sheets with offset '{}'; limit '{}'; sort '{}'; filter '{}'",
				query.getOffset(), query.getLimit(), query.getSortOrders(), query.getFilter().get().toString());
		if (query == null || query.getFilter().isEmpty()) {
			log.debug("Get Archived Route Sheets is impossible. Miss some data.");
			return Stream.empty();
		}
		try {
			return dao.findAll(queryToSort(query))
					.stream()
					.filter(entity -> query.getFilter().get().isPass(entity))
					.skip(query.getOffset())
					.limit(query.getLimit());
		} catch (Exception e) {
			log.error("The Archived Route Sheets list was not received for a reason: {}", e.getMessage());
		}
		return Stream.empty();
	}

	/**
	 * Gget queried archived sheets by filter count.
	 *
	 * @param query the query
	 * @return the int
	 */
	@Override
	public int getQueriedArchivedSheetsByFilterCount(
			Query<ArchivedRouteSheet, EntityFilter<ArchivedRouteSheet>> query) {
		log.debug("Get requested size Archived Route Sheet with filter '{}'", query.getFilter().get().toString());
		return (int) getQueriedArchivedSheetsByFilter(query).count();
	}
}