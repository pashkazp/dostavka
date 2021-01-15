package ua.com.sipsoft.service.request.archive;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import ua.com.sipsoft.dao.request.archive.ArchivedRouteSheet;
import ua.com.sipsoft.service.util.EntityFilter;
import ua.com.sipsoft.util.query.Query;

/**
 * The Interface ArchivedSheetsService.
 *
 * @author Pavlo Degtyaryev
 */
@Service
public interface ArchivedSheetsService {

	/**
	 * Save.
	 *
	 * @param archivedRouteSheet the archived route sheet
	 * @return the optional
	 */
	Optional<ArchivedRouteSheet> save(ArchivedRouteSheet archivedRouteSheet);

	/**
	 * Gets the queried archived sheets by filter.
	 *
	 * @param query the query
	 * @return the queried archived sheets by filter
	 */
	Stream<ArchivedRouteSheet> getQueriedArchivedSheetsByFilter(
			Query<ArchivedRouteSheet, EntityFilter<ArchivedRouteSheet>> query);

	/**
	 * Gets the queried archived sheets by filter count.
	 *
	 * @param query the query
	 * @return the queried archived sheets by filter count
	 */
	int getQueriedArchivedSheetsByFilterCount(Query<ArchivedRouteSheet, EntityFilter<ArchivedRouteSheet>> query);
}