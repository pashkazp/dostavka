package ua.com.sipsoft.service.request.draft;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.service.dto.request.CourierRequestDto;
import ua.com.sipsoft.service.security.UserPrincipal;
import ua.com.sipsoft.util.paging.Page;
import ua.com.sipsoft.util.paging.PagingRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class DraftRouteReqsService {

	private final DraftRouteReqsServiceToRepo draftRouteReqsServiceToRepo;

	public Page<CourierRequestDto> getFilteredPage(@NonNull PagingRequest pagingRequest,
			@NonNull CourierRequestFilter courierRequestFilter) {
		courierRequestFilter
				.setCaller(((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
						.getUser());
		return draftRouteReqsServiceToRepo.getFilteredPage(pagingRequest, courierRequestFilter);
	}
}
