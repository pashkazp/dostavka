package ua.com.sipsoft.ui.rest.v1.requests.draft;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.dto.request.CourierRequestDto;
import ua.com.sipsoft.service.dto.request.DraftSheetUpdReqDto;
import ua.com.sipsoft.service.dto.request.RouteSheetDto;
import ua.com.sipsoft.service.request.draft.DraftRouteSheetService;
import ua.com.sipsoft.service.request.draft.DraftSheetFilter;
import ua.com.sipsoft.ui.model.request.mapper.ToDraftSheetUpdDtoMapper;
import ua.com.sipsoft.ui.model.request.request.draft.DraftSheetUpdReq;
import ua.com.sipsoft.ui.model.response.InfoResponse;
import ua.com.sipsoft.ui.model.response.mapper.DraftReqsRespMapper;
import ua.com.sipsoft.ui.model.response.mapper.DraftSheetRespMapper;
import ua.com.sipsoft.ui.model.response.request.draft.DraftReqsCardResponse;
import ua.com.sipsoft.ui.model.response.request.draft.DraftSheetCardResp;
import ua.com.sipsoft.util.AppURL;
import ua.com.sipsoft.util.I18NProvider;
import ua.com.sipsoft.util.message.RestV1Msg;
import ua.com.sipsoft.util.paging.Page;
import ua.com.sipsoft.util.paging.PagingRequest;

@Slf4j
@RestController
@RequestMapping(AppURL.API_V1_DRAFT_RSHEET)
@RequiredArgsConstructor
public class DraftSheetController {

	private final DraftRouteSheetService draftRouteSheetService;
	private final DraftSheetRespMapper draftSheetRespMapper;
	private final DraftReqsRespMapper draftReqsRespMapper;
	private final ToDraftSheetUpdDtoMapper toDraftSheetUpdDtoMapper;
	private final I18NProvider i18n;

	// Paging for route sheet
	@PostMapping(value = AppURL.PAGES, consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_PRODUCTOPER" })
	public ResponseEntity<Object> getDraftSheetPages(@RequestBody(required = true) PagingRequest pagingRequest,
			@AuthenticationPrincipal User user) {

		log.debug("getDraftSheetPages] - get page of draft sheets by request: {}", pagingRequest.toString());

		DraftSheetFilter draftSheetFilter = new DraftSheetFilter();
		if (StringUtils.isNotBlank(pagingRequest.getSearch().getValue())) {
			draftSheetFilter.setDescription(pagingRequest.getSearch().getValue());
		}

		Page<RouteSheetDto> page = draftRouteSheetService.getFilteredPage(pagingRequest, draftSheetFilter);

		List<RouteSheetDto> data = page.getData();
		List<DraftSheetCardResp> draftSheetReqsList = draftSheetRespMapper.toRest(data);
		Page<DraftSheetCardResp> pageRest = new Page<>(page, draftSheetReqsList);

		return new ResponseEntity<>(pageRest, HttpStatus.PARTIAL_CONTENT);
	}

	// Paging for sheet requests
	@PostMapping(value = "/{id}" + AppURL.REQUESTS, produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER", "ROLE_PRODUCTOPER" })
	public ResponseEntity<Object> getRequestsBySheetId(@PathVariable(value = "id") Long sheetId,
			@RequestBody(required = true) PagingRequest pagingRequest) {

		log.info("getRequestsBySheetId] - Request the requests of the Draft sheet by its ID='{}'",
				sheetId);

		Optional<RouteSheetDto> routeSheetDtoO;
		if (sheetId < 1) {
			routeSheetDtoO = Optional.empty();
		} else {
			routeSheetDtoO = draftRouteSheetService.getRouteSheetDto(sheetId);
		}

		Page<DraftReqsCardResponse> page = new Page<>();
		page.setDraw(0);
		page.setRecordsTotal(0);
		page.setRecordsFiltered(0);
		page.setData(List.of());

		if (routeSheetDtoO.isEmpty()) {
			return ResponseEntity.ok(page);
		}

		List<CourierRequestDto> response = routeSheetDtoO.get().getRequests().stream()
				.collect(Collectors.toList());
		if (!response.isEmpty()) {
			List<DraftReqsCardResponse> resp = draftReqsRespMapper.toRest(response);
			page.setRecordsTotal(resp.size());
			page.setRecordsFiltered(resp.size());
			page.setData(resp);
		}

		return ResponseEntity.ok(page);
	}

	// Get Route sheet
	@GetMapping(value = "/{routeSheetId}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_PRODUCTOPER" })
	public ResponseEntity<Object> getRouteSheetById(@PathVariable(value = "routeSheetId") Long routeSheetId) {

		log.debug("getRouteSheetById] - Get Route Sheet by id {}", routeSheetId);

		Optional<RouteSheetDto> routeSheetDtoO = draftRouteSheetService.getRouteSheetDto(routeSheetId);

		if (routeSheetDtoO.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		DraftSheetCardResp routeSheet = draftSheetRespMapper.toRest(routeSheetDtoO.get());
		return ResponseEntity.ok(routeSheet);
	}

	// Put route sheet
	@PutMapping(value = "/{routeSheetId}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER" })
	public ResponseEntity<Object> updateRouteSheet(@PathVariable(value = "routeSheetId") Long routeSheetId,
			@RequestBody(required = false) DraftSheetUpdReq draftSheetUpdReq, Locale loc) {

		log.info("updateRouteSheet] - Request Draft Route Sheet update id='{}' by Route Sheet data '{}'", routeSheetId,
				draftSheetUpdReq);
		if (draftSheetUpdReq == null) {
			log.warn("updateRouteSheet] - Draft Route Sheet update request must be not null");
			InfoResponse infoResponse = new InfoResponse(HttpStatus.BAD_REQUEST,
					i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST, loc),
					i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST_EXT, loc));

			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());
		}

		draftSheetUpdReq.setId(routeSheetId);
		DraftSheetUpdReqDto draftSheetUpdReqDto = toDraftSheetUpdDtoMapper.fromDraftSheetUpdReq(draftSheetUpdReq);
		log.info("updateRouteSheet] - Perform update Draft Route Sheet");
		Optional<RouteSheetDto> routeSheetDtoO = draftRouteSheetService
				.updateRouteSheet(draftSheetUpdReqDto);

		if (routeSheetDtoO.isEmpty()) {
			log.info("updateRouteSheet] - Update is fail. Inform to the registrant");
			InfoResponse infoResponse = new InfoResponse(HttpStatus.INTERNAL_SERVER_ERROR,
					i18n.getTranslation(RestV1Msg.ROUTESHEET_UPDATE_FAIL, loc),
					i18n.getTranslation(RestV1Msg.ROUTESHEET_UPDATE_FAIL_EXT, loc));

			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());

		}

		log.info("updateRouteSheet] - Update is successful. Inform to the updater");
		DraftSheetCardResp draftSheetCardResp = draftSheetRespMapper.toRest(routeSheetDtoO.get());
		return ResponseEntity.accepted().body(draftSheetCardResp);
	}

}
