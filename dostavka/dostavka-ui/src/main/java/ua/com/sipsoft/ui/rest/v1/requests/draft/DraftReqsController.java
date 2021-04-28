package ua.com.sipsoft.ui.rest.v1.requests.draft;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.dto.request.CourierRequestDto;
import ua.com.sipsoft.service.request.draft.CourierRequestFilter;
import ua.com.sipsoft.service.request.draft.DraftRouteReqsService;
import ua.com.sipsoft.ui.model.response.mapper.DraftReqsRespMapper;
import ua.com.sipsoft.ui.model.response.request.draft.DraftReqsCardResponse;
import ua.com.sipsoft.util.AppURL;
import ua.com.sipsoft.util.paging.Page;
import ua.com.sipsoft.util.paging.PagingRequest;

@Slf4j
@RestController
@RequestMapping(AppURL.API_V1_DRAFT_REQ)
@RequiredArgsConstructor
public class DraftReqsController {

	private final DraftRouteReqsService draftRouteReqsService;
	private final DraftReqsRespMapper draftReqsRespMapper;

	@PostMapping(value = AppURL.PAGES, consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER", "ROLE_PRODUCTOPER", "ROLE_COURIER",
			"ROLE_CLIENT" })
	public ResponseEntity<Object> getDraftReqsPages(@RequestBody(required = true) PagingRequest pagingRequest,
			@AuthenticationPrincipal User user) {

		log.debug("getDraftReqsPages] - get page of draft sheets by request: {}", pagingRequest.toString());

		CourierRequestFilter courierRequestFilter = new CourierRequestFilter();
		if (StringUtils.isNotBlank(pagingRequest.getSearch().getValue())) {
			courierRequestFilter.setDescription(pagingRequest.getSearch().getValue());
		}

		Page<CourierRequestDto> page = draftRouteReqsService.getFilteredPage(pagingRequest, courierRequestFilter);

		List<CourierRequestDto> data = page.getData();
		List<DraftReqsCardResponse> draftReqsList = draftReqsRespMapper.toRest(data);
		Page<DraftReqsCardResponse> pageRest = new Page<>(page, draftReqsList);

		return new ResponseEntity<>(pageRest, HttpStatus.PARTIAL_CONTENT);
	}

}
