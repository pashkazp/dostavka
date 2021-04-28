package ua.com.sipsoft.service.request.draft;

import org.springframework.stereotype.Service;

import lombok.NonNull;
import ua.com.sipsoft.service.dto.request.CourierRequestDto;
import ua.com.sipsoft.service.util.EntityFilter;
import ua.com.sipsoft.util.paging.Page;
import ua.com.sipsoft.util.paging.PagingRequest;

/**
 * The Class DraftRouteSheetServiceToRepo.
 *
 * @author Pavlo Degtyaryev
 */
@Service
public interface DraftRouteReqsServiceToRepo {

	Page<CourierRequestDto> getFilteredPage(@NonNull PagingRequest pagingRequest,
			@NonNull EntityFilter<CourierRequestDto> entityFilter);

}