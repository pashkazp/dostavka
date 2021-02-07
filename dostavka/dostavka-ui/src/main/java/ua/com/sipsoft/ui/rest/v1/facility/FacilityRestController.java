package ua.com.sipsoft.ui.rest.v1.facility;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.collect.Multimap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.common.FacilitiesFilter;
import ua.com.sipsoft.service.common.FacilityService;
import ua.com.sipsoft.service.dto.facility.FacilityAddrDto;
import ua.com.sipsoft.service.dto.facility.FacilityDto;
import ua.com.sipsoft.service.dto.facility.FacilityRegReqDto;
import ua.com.sipsoft.service.dto.facility.FacilityUpdReqDto;
import ua.com.sipsoft.service.exception.FacilityDtoAuditExeption;
import ua.com.sipsoft.service.exception.FacilityNotFoundException;
import ua.com.sipsoft.service.exception.ResourceNotFoundException;
import ua.com.sipsoft.ui.model.request.facility.FacilityAddrRegReq;
import ua.com.sipsoft.ui.model.request.facility.FacilityRegReq;
import ua.com.sipsoft.ui.model.request.facility.FacilityUpdReq;
import ua.com.sipsoft.ui.model.request.mapper.ToFacilityRegDtoMapper;
import ua.com.sipsoft.ui.model.request.mapper.ToFacilityUpdDtoMapper;
import ua.com.sipsoft.ui.model.response.AbstractSubInfoResponse;
import ua.com.sipsoft.ui.model.response.InfoResponse;
import ua.com.sipsoft.ui.model.response.ValidationInfoResponse;
import ua.com.sipsoft.ui.model.response.facility.FacilityAddrResponse;
import ua.com.sipsoft.ui.model.response.facility.FacilityResponse;
import ua.com.sipsoft.ui.model.response.mapper.FacilityAddrRespMapper;
import ua.com.sipsoft.ui.model.response.mapper.FacilityRespMapper;
import ua.com.sipsoft.util.AppURL;
import ua.com.sipsoft.util.I18NProvider;
import ua.com.sipsoft.util.message.RestV1Msg;
import ua.com.sipsoft.util.paging.Page;
import ua.com.sipsoft.util.paging.PagingRequest;

@Slf4j
@RestController
@RequestMapping(AppURL.API_V1_FACILITIES)
@RequiredArgsConstructor
public class FacilityRestController {

	/** The facilities service. */
	private final FacilityService facilitiesService;

	/** The facility addr rest controller. */
	private final FacilityAddrRestController facilityAddrRestController;

	/** The i18n provider. */
	private final I18NProvider i18n;
	private final ToFacilityRegDtoMapper toFacilityRegDtoMapper;
	private final ToFacilityUpdDtoMapper toFacilityUpdDtoMapper;
	private final FacilityRespMapper facilityRespMapper;
	private final FacilityAddrRespMapper facilityAddrRespMapper;

	/**
	 * Return List all {@link FacilityDto}.
	 *
	 * @return the {@link List} of {@link FacilityDto}
	 */
	@GetMapping(value = "", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Object> listAllFacilitiesDto() {
		log.debug("Get the list of  all FacilityDto");
		List<FacilityDto> facilities = facilitiesService.getAllFacilitiesDto();
		if (!facilities.isEmpty()) {
			// TODO disabled by access violation to user password
			// return ResponseEntity.ok(facilities);
		}
		return ResponseEntity.notFound().build();
	}

	/**
	 * Gets the page of Fasilities.
	 *
	 * @param pagingRequest the request of Page of Facilities
	 * @return the fasilities page
	 */
	@PostMapping(value = AppURL.PAGES, consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER", "ROLE_PRODUCTOPER", "ROLE_COURIER",
			"ROLE_CLIENT" })
	public ResponseEntity<Object> getFasilitiesPage(@RequestBody(required = true) PagingRequest pagingRequest,
			@AuthenticationPrincipal User user) {
		log.debug("getFasilitiesPage] - get page of facilities by request: {}", pagingRequest.toString());

		FacilitiesFilter facilityFilter = new FacilitiesFilter();
		if (StringUtils.isNotBlank(pagingRequest.getSearch().getValue())) {
			facilityFilter.setName(pagingRequest.getSearch().getValue());
		}

		Page<FacilityDto> page = facilitiesService.getFilteredPage(pagingRequest, facilityFilter);

		List<FacilityDto> data = page.getData();
		List<FacilityResponse> fasilitiesList = facilityRespMapper.toRest(data);
		Page<FacilityResponse> pageRest = new Page<>(page, fasilitiesList);

		return new ResponseEntity<>(pageRest, HttpStatus.PARTIAL_CONTENT);

	}

	/**
	 * Gets the {@link FacilityRegReqDto}.
	 *
	 * @param facilityId the facility id
	 * @return the {@link FacilityRegReqDto}
	 */
	@GetMapping(value = "/{facilityId}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER", "ROLE_PRODUCTOPER", "ROLE_COURIER",
			"ROLE_CLIENT" })
	public ResponseEntity<Object> getFacilityById(@PathVariable(value = "facilityId") Long facilityId) {

		log.debug("getFacilityById] - Get Facility by id {}", facilityId);

		Optional<FacilityDto> facilityDtoO = facilitiesService.getFacilityDtoById(facilityId);

		if (facilityDtoO.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		FacilityResponse fasility = facilityRespMapper.toRest(facilityDtoO.get());
		return ResponseEntity.ok(fasility);
	}

	/**
	 * Adds the new Facility.
	 *
	 * @param newFacility the new Facility
	 * @param loc         the Locale
	 * @param principal   the Principal
	 * @return the response entity
	 */
	@PostMapping(value = "", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER" })
	public ResponseEntity<Object> addNewFacility(
			@RequestBody(required = false) FacilityRegReq newFacility, Locale loc,
			Principal principal) {

		log.info("addNewFacility] - Request register new Facility: '{}'", newFacility);

		if (newFacility == null) {
			log.warn("addNewFacility] - Facility creation request must be not null");
			InfoResponse infoResponse = new InfoResponse(HttpStatus.BAD_REQUEST,
					i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST, loc),
					i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST_EXT, loc));

			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());
		}

		FacilityRegReqDto facilityRegDto = toFacilityRegDtoMapper
				.fromFacilityRegReq(newFacility);

		log.info("addNewFacility] - Perform register Facility");

		Optional<FacilityDto> facilityDtoO = facilitiesService.registerNewFacility(facilityRegDto);

		if (facilityDtoO.isEmpty()) { // Registration is fail
			log.info("addNewFacility] - Registration is fail. Inform to the registrant");
			InfoResponse infoResponse = new InfoResponse(HttpStatus.INTERNAL_SERVER_ERROR,
					i18n.getTranslation(RestV1Msg.INFO_INACESSIBLE, loc),
					i18n.getTranslation(RestV1Msg.INFO_INACESSIBLE_EXT, loc));

			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());
		}

		log.info("addNewFacility] - Registration is successful. Inform to the registrant");
		FacilityResponse facilityResponse = facilityRespMapper.toRest(facilityDtoO.get());
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path(AppURL.API_V1_FACILITIES).path("/{id}")
				.buildAndExpand(facilityDtoO.get().getId()).toUri();
		return ResponseEntity.created(location).body(facilityResponse);

	}

	/**
	 * Update facility.
	 *
	 * @param facilityId      the facility id
	 * @param updatedFacility the updated facility
	 * @param loc             the loc
	 * @param principal       the principal
	 * @return the response entity
	 */
	@PutMapping(value = "/{facilityId}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER" })
	public ResponseEntity<Object> updateFacility(@PathVariable(value = "facilityId") Long facilityId,
			@RequestBody(required = false) FacilityUpdReq updatedFacility, Locale loc) {

		log.info("updateFacility] - Request Facility update id='{}' by Facility data '{}'", facilityId,
				updatedFacility);
		if (updatedFacility == null) {
			log.warn("updateFacility] - Facility update request must be not null");
			InfoResponse infoResponse = new InfoResponse(HttpStatus.BAD_REQUEST,
					i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST, loc),
					i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST_EXT, loc));

			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());
		}

		FacilityUpdReqDto facilityUpdDto = toFacilityUpdDtoMapper
				.fromFacilityUpdReq(updatedFacility);

		facilityUpdDto.setId(facilityId);

		log.info("updateFacility] - Perform update Facility");
		Optional<FacilityDto> facilityDtoO = facilitiesService.updateFacility(facilityUpdDto);

		if (facilityDtoO.isEmpty()) {
			log.info("updateFacility] - Update is fail. Inform to the registrant");
			InfoResponse infoResponse = new InfoResponse(HttpStatus.INTERNAL_SERVER_ERROR,
					i18n.getTranslation(RestV1Msg.FACILITY_UPDATE_FAIL, loc),
					i18n.getTranslation(RestV1Msg.FACILITY_UPDATE_FAIL_EXT, loc));

			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());

		}

		log.info("updateFacility] - Update is successful. Inform to the updater");
		FacilityResponse facilityResponse = facilityRespMapper.toRest(facilityDtoO.get());
		return ResponseEntity.accepted().body(facilityResponse);
	}

	@DeleteMapping(value = "/{facilityId}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER" })
	public ResponseEntity<Object> deleteFacility(@PathVariable(value = "facilityId") Long facilityId) {

		log.info("deleteFacility] - Request Facility delete by id='{}'", facilityId);

		facilitiesService.deleteFacility(facilityId);
		return ResponseEntity.ok().build();
	}

	/**
	 * Adds the new Facility Address.
	 *
	 * @param facilityId          the Facility id
	 * @param facilityAddrDtoJson the Facility addr dto json
	 * @param loc                 the Locale
	 * @param principal           the Principal
	 * @return the response entity
	 */
	@PostMapping(value = "/{id}" + AppURL.FACILITIESADDR, consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER" })
	public ResponseEntity<Object> addNewFacilityAddr(@PathVariable(value = "id") Long facilityId,
			@RequestBody(required = false) FacilityAddrRegReq newFacilityAddr) {
		log.debug("addNewFacilityAddr] - try to  add new fasility address: {}", newFacilityAddr);
		ResponseEntity<Object> response = facilityAddrRestController.addNewFacilityAddr(facilityId, newFacilityAddr);
		return response;
	}

	/**
	 * Gets the facility addresses by facility id.
	 *
	 * @param facilityId the facility id
	 * @param loc        the loc
	 * @param principal  the principal
	 * @return the facility addresses by facility id
	 */
	@GetMapping(value = "/{id}" + AppURL.FACILITIESADDR, produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER", "ROLE_PRODUCTOPER", "ROLE_COURIER",
			"ROLE_CLIENT" })
	public ResponseEntity<Object> getFacilityAddressesByFacilityId(@PathVariable(value = "id") Long facilityId) {

		log.info("getFacilityAddressesByFacilityId] - Request the addresses of the Facility by its ID='{}'",
				facilityId);

		Optional<FacilityDto> facilityDtoO = facilitiesService.getFacilityDtoById(facilityId);

		if (facilityDtoO.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		List<FacilityAddrDto> response = facilityDtoO.get().getFacilityAddresses().stream()
				.collect(Collectors.toList());
		if (!response.isEmpty()) {
			List<FacilityAddrResponse> resp = facilityAddrRespMapper.toRest(response);
			return ResponseEntity.ok(resp);
		} else
			return ResponseEntity.ok(List.of());

	}

	/**
	 * Handle facility dto audit exeption.
	 *
	 * @param ex      the ex
	 * @param request the request
	 * @param loc     the loc
	 * @return the response entity
	 */
	@ExceptionHandler(value = { FacilityDtoAuditExeption.class })
	@ResponseBody()
	public ResponseEntity<Object> handleFacilityDtoAuditExeption(FacilityDtoAuditExeption ex, WebRequest request,
			Locale loc) {
		log.debug("IN handleFacilityDtoAuditExeption - Gets exception: {}", ex.getMessage());

		InfoResponse infoResponse = new InfoResponse(HttpStatus.BAD_REQUEST,
				i18n.getTranslation(RestV1Msg.FACILITY_NEW_CHECK_FAIL, loc),
				i18n.getTranslation(RestV1Msg.FACILITY_NEW_CHECK_FAIL_EXT, loc));

		Multimap<String, String> response = ex.getAuditMessages();

		if (!response.isEmpty()) {
			infoResponse.setSubInfos(new ArrayList<AbstractSubInfoResponse>());
			response.forEach((k, v) -> infoResponse.getSubInfos().add(new ValidationInfoResponse(k, v)));
		}

		infoResponse.getSubInfos().add(new ValidationInfoResponse("", infoResponse.getMessage()));

		String headers = request.getHeader(HttpHeaders.ACCEPT);

		MediaType mt;
		if (headers.indexOf(MediaType.APPLICATION_JSON_VALUE) == -1) {
			mt = MediaType.APPLICATION_XML;
		} else {
			mt = MediaType.APPLICATION_JSON;
		}
		return ResponseEntity.status(infoResponse.getStatus()).contentType(mt).body(infoResponse);
	}

	/**
	 * Handle other exceptions.
	 *
	 * @param ex      the ex
	 * @param request the request
	 * @return the response entity
	 */
	@ExceptionHandler(value = { Exception.class })
	@ResponseBody()
	public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {
		log.debug("handleOtherExceptions] - Gets exception: {}", ex.getMessage());

		InfoResponse infoResponse = new InfoResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);

		String headers = request.getHeader(HttpHeaders.ACCEPT);

		MediaType mt;
		if (headers.indexOf(MediaType.APPLICATION_JSON_VALUE) == -1) {
			mt = MediaType.APPLICATION_XML;
		} else {
			mt = MediaType.APPLICATION_JSON;
		}
		return ResponseEntity.status(infoResponse.getStatus()).contentType(mt).body(infoResponse);
	}

	@ExceptionHandler(value = { ResourceNotFoundException.class })
	@ResponseBody()
	public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request,
			Locale locale) {
		log.debug("handleResourceNotFoundException] - Gets exception: {}", ex.getMessage());

		InfoResponse infoResponse = new InfoResponse(HttpStatus.NOT_FOUND,
				i18n.getTranslation(RestV1Msg.FACILITY_NOTFOUND, locale),
				i18n.getTranslation(RestV1Msg.FACILITY_NOTFOUND_EXT, locale));

		String headers = request.getHeader(HttpHeaders.ACCEPT);

		MediaType mt;
		if (headers.indexOf(MediaType.APPLICATION_JSON_VALUE) == -1) {
			mt = MediaType.APPLICATION_XML;
		} else {
			mt = MediaType.APPLICATION_JSON;
		}
		return ResponseEntity.status(infoResponse.getStatus()).contentType(mt).body(infoResponse);
	}

	@ExceptionHandler(value = { FacilityNotFoundException.class })
	@ResponseBody()
	public ResponseEntity<Object> handleFacilityNotFoundException(FacilityNotFoundException ex,
			WebRequest request) {
		log.debug("handleFacilityAddrNotFoundException] - Gets exception: {}", ex.getMessage());

		InfoResponse infoResponse = new InfoResponse(HttpStatus.NOT_FOUND, ex.getErrMsg(), ex.getErrMsgExt());

		String headers = request.getHeader(HttpHeaders.ACCEPT);

		MediaType mt;
		if (headers.indexOf(MediaType.APPLICATION_JSON_VALUE) == -1) {
			mt = MediaType.APPLICATION_XML;
		} else {
			mt = MediaType.APPLICATION_JSON;
		}
		return ResponseEntity.status(infoResponse.getStatus()).contentType(mt).body(infoResponse);
	}
}
