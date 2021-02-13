package ua.com.sipsoft.ui.rest.v1.requests.draft;

import java.util.List;

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
import ua.com.sipsoft.service.dto.request.RouteSheetDto;
import ua.com.sipsoft.service.request.draft.DraftRouteSheetService;
import ua.com.sipsoft.service.request.draft.DraftSheetFilter;
import ua.com.sipsoft.ui.model.response.mapper.DraftSheetRespMapper;
import ua.com.sipsoft.ui.model.response.request.daft.DraftSheetCardResponse;
import ua.com.sipsoft.util.AppURL;
import ua.com.sipsoft.util.paging.Page;
import ua.com.sipsoft.util.paging.PagingRequest;

@Slf4j
@RestController
@RequestMapping(AppURL.API_V1_DRAFT_RSHEET)
@RequiredArgsConstructor
public class DraftSheetController {

	private final DraftRouteSheetService draftRouteSheetService;
	private final DraftSheetRespMapper draftSheetRespMapper;

	@PostMapping(value = AppURL.PAGES, consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
//	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER", "ROLE_PRODUCTOPER", "ROLE_COURIER",
//			"ROLE_CLIENT" })
	public ResponseEntity<Object> getDraftSheetPages(@RequestBody(required = true) PagingRequest pagingRequest,
			@AuthenticationPrincipal User user) {

		log.debug("getDraftSheetPages] - get page of draft sheets by request: {}", pagingRequest.toString());

		DraftSheetFilter draftSheetFilter = new DraftSheetFilter();
		if (StringUtils.isNotBlank(pagingRequest.getSearch().getValue())) {
			draftSheetFilter.setDescription(pagingRequest.getSearch().getValue());
		}

		Page<RouteSheetDto> page = draftRouteSheetService.getFilteredPage(pagingRequest, draftSheetFilter);

		List<RouteSheetDto> data = page.getData();
		List<DraftSheetCardResponse> fasilitiesList = draftSheetRespMapper.toRest(data);
		Page<DraftSheetCardResponse> pageRest = new Page<>(page, fasilitiesList);

		return new ResponseEntity<>(pageRest, HttpStatus.PARTIAL_CONTENT);
	}

}
