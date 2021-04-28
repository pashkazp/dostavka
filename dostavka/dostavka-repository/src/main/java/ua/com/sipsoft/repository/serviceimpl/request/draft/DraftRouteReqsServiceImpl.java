package ua.com.sipsoft.repository.serviceimpl.request.draft;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.request.draft.CourierRequest;
import ua.com.sipsoft.repository.request.draft.CourierRequestRepository;
import ua.com.sipsoft.repository.serviceimpl.mapper.request.CourierRequestMapper;
import ua.com.sipsoft.service.dto.request.CourierRequestDto;
import ua.com.sipsoft.service.request.draft.DraftRouteReqsServiceToRepo;
import ua.com.sipsoft.service.util.EntityFilter;
import ua.com.sipsoft.service.util.HasFilteredList;
import ua.com.sipsoft.service.util.HasLimitedList;
import ua.com.sipsoft.service.util.HasPagingRequestToSortConvertor;
import ua.com.sipsoft.util.paging.Page;
import ua.com.sipsoft.util.paging.PagingRequest;

/**
 * The Class DraftRouteSheetServiceImpl.
 *
 * @author Pavlo Degtyaryev
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DraftRouteReqsServiceImpl
		implements DraftRouteReqsServiceToRepo, HasPagingRequestToSortConvertor, HasFilteredList, HasLimitedList {

	private final CourierRequestRepository dao;
	private final CourierRequestMapper courierRequestMapper;

	@Override
	public Page<CourierRequestDto> getFilteredPage(@NonNull PagingRequest pagingRequest,
			@NonNull EntityFilter<CourierRequestDto> entityFilter) {
		log.debug(
				"getFilteredPage] - Get requested page Draft requests with PagingRequest '{}' and EntityFilter<Facility> '{}'",
				pagingRequest, entityFilter);

		Page<CourierRequestDto> page = new Page<>();
		List<CourierRequest> routeReqs;
		routeReqs = dao.findAll(toSort(pagingRequest));

		List<CourierRequestDto> routeReqsDto = getFiteredList(courierRequestMapper.courierRequestToDto(routeReqs),
				entityFilter);

		page.setRecordsTotal(routeReqsDto.size());

		page.setRecordsFiltered(routeReqsDto.size());

		routeReqsDto = getLimitedList(routeReqsDto, pagingRequest.getStart(), pagingRequest.getLength());

		page.setData(routeReqsDto);

		page.setDraw(pagingRequest.getDraw());

		return page;
	}
}