package ua.com.sipsoft.service.request.draft;

import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.service.dto.request.RouteSheetDto;
import ua.com.sipsoft.util.paging.Page;
import ua.com.sipsoft.util.paging.PagingRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class DraftRouteSheetService {

	private final DraftRouteSheetServiceToRepo draftRouteSheetServiceToRepo;

	public Page<RouteSheetDto> getFilteredPage(@NonNull PagingRequest pagingRequest,
			@NonNull DraftSheetFilter draftSheetFilter) {
		return draftRouteSheetServiceToRepo.getFilteredPage(pagingRequest, draftSheetFilter);
	}
}
