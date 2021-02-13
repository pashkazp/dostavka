package ua.com.sipsoft.service.request.draft;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import lombok.NonNull;
import ua.com.sipsoft.dao.request.draft.CourierRequest;
import ua.com.sipsoft.dao.request.draft.DraftRouteSheet;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.dto.request.RouteSheetDto;
import ua.com.sipsoft.service.util.EntityFilter;
import ua.com.sipsoft.util.paging.Page;
import ua.com.sipsoft.util.paging.PagingRequest;
import ua.com.sipsoft.util.query.Query;

/**
 * The Class DraftRouteSheetServiceToRepo.
 *
 * @author Pavlo Degtyaryev
 */
@Service
public interface DraftRouteSheetServiceToRepo {

	/**
	 * Clean draft sheets from courier requests.
	 *
	 * @param requests the requests
	 */
	void cleanDraftSheetsFromCourierRequests(Collection<CourierRequest> requests);

	/**
	 * Removes the requests and save.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @param requests        the requests
	 * @return the optional
	 */
	Optional<DraftRouteSheet> removeRequestsAndSave(DraftRouteSheet draftRouteSheet,
			Collection<CourierRequest> requests);

	/**
	 * Creates the and save draft route sheet.
	 *
	 * @param requests    the requests
	 * @param description the description
	 * @param author      the author
	 * @return the optional
	 */
	Optional<DraftRouteSheet> createAndSaveDraftRouteSheet(Collection<CourierRequest> requests, String description,
			User author);

	/**
	 * Save.
	 *
	 * @param request the request
	 * @return the optional
	 */
	Optional<DraftRouteSheet> save(DraftRouteSheet request);

	/**
	 * Move to archive.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @param movingReason    the moving reason
	 * @param author          the author
	 */
	void moveToArchive(DraftRouteSheet draftRouteSheet, String movingReason, User author);

	/**
	 * Adds the courier requests and save.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @param author          the author
	 * @param request         the request
	 * @return the optional
	 */
	Optional<DraftRouteSheet> addCourierRequestsAndSave(DraftRouteSheet draftRouteSheet, User author,
			CourierRequest... request);

	/**
	 * Adds the requests and save.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @param requests        the requests
	 * @param author          the author
	 * @return the optional
	 */
	Optional<DraftRouteSheet> addRequestsAndSave(DraftRouteSheet draftRouteSheet, Collection<CourierRequest> requests,
			User author);

	/**
	 * Issue draft route sheet.
	 *
	 * @param draftRouteSheet the draft route sheet
	 * @param description     the description
	 * @param author          the author
	 */
	void issueDraftRouteSheet(DraftRouteSheet draftRouteSheet, String description, User author);

	/**
	 * Gets the queried draft route sheets.
	 *
	 * @param query the query
	 * @return the queried draft route sheets
	 */
	Stream<DraftRouteSheet> getQueriedDraftRouteSheets(Query<DraftRouteSheet, EntityFilter<DraftRouteSheet>> query);

	/**
	 * Gets the queried draft route sheets count.
	 *
	 * @param query the query
	 * @return the queried draft route sheets count
	 */
	int getQueriedDraftRouteSheetsCount(Query<DraftRouteSheet, EntityFilter<DraftRouteSheet>> query);

	/**
	 * Fetch by id.
	 *
	 * @param id the id
	 * @return the optional
	 */
	Optional<DraftRouteSheet> fetchById(Long id);

	Page<RouteSheetDto> getFilteredPage(@NonNull PagingRequest pagingRequest,
			@NonNull EntityFilter<DraftRouteSheet> entityFilter);

}