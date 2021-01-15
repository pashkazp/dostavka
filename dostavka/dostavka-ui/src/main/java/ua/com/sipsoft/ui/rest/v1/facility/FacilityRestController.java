package ua.com.sipsoft.ui.rest.v1.facility;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import ua.com.sipsoft.dao.common.Facility;
import ua.com.sipsoft.service.common.FacilitiesFilter;
import ua.com.sipsoft.service.common.FacilitiesService;
import ua.com.sipsoft.service.dto.facility.FacilityAddressDto;
import ua.com.sipsoft.service.dto.facility.FacilityDto;
import ua.com.sipsoft.service.dto.facility.FacilityRegistrationDto;
import ua.com.sipsoft.service.dto.facility.FacilityUpdateDto;
import ua.com.sipsoft.service.exception.FacilityDtoAuditExeption;
import ua.com.sipsoft.service.exception.UserDtoAuditExeption;
import ua.com.sipsoft.ui.model.request.facility.FacilityRegistrationRequest;
import ua.com.sipsoft.ui.model.request.facility.FacilityUpdateRequest;
import ua.com.sipsoft.ui.model.request.mapper.ToFacilityRegistrationDtoMapper;
import ua.com.sipsoft.ui.model.request.mapper.ToFacilityUpdateDtoMapper;
import ua.com.sipsoft.ui.model.response.AbstractSubInfoResponse;
import ua.com.sipsoft.ui.model.response.InfoResponse;
import ua.com.sipsoft.ui.model.response.ValidationInfoResponse;
import ua.com.sipsoft.ui.model.response.facility.FacilityResponse;
import ua.com.sipsoft.ui.model.response.mapper.FacilityRespMapper;
import ua.com.sipsoft.util.AppURL;
import ua.com.sipsoft.util.I18NProvider;
import ua.com.sipsoft.util.message.RestV1Msg;
import ua.com.sipsoft.util.paging.Page;
import ua.com.sipsoft.util.paging.PagingRequest;

@Slf4j
@RestController
@RequestMapping(AppURL.API_V1_FACILITIES)

/**
 * Instantiates a new facility rest controller.
 *
 * @param facilitiesService          the facilities service
 * @param facilityAddrRestController the facility addr rest controller
 * @param i18n                       the i 18 n
 */
@RequiredArgsConstructor
public class FacilityRestController {

	/** The facilities service. */
	private final FacilitiesService facilitiesService;

	/** The facility addr rest controller. */
	private final FacilityAddrRestController facilityAddrRestController;

	/** The i18n provider. */
	private final I18NProvider i18n;

	/**
	 * Return List all {@link FacilityDto}.
	 *
	 * @return the {@link List} of {@link FacilityDto}
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> listAllFacilitiesDto() {
		log.debug("Get the list of  all FacilityDto");
		// TODO return only Facilities approved for Editor
		List<FacilityDto> facilities = facilitiesService.getAllFacilitiesDto();
		if (!facilities.isEmpty()) {
			return ResponseEntity.ok(facilities);
		}
		return ResponseEntity.notFound().build();
	}

	/**
	 * Gets the page of Fasilities.
	 *
	 * @param pagingRequest the request of Page of Facilities
	 * @return the fasilities page
	 */
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = AppURL.PAGES, consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getFasilitiesPage(@RequestBody(required = true) PagingRequest pagingRequest) {
		log.debug("IN getFasilitiesPage - get page of facilities by request: {}", pagingRequest.toString());

		FacilitiesFilter facilityFilter = new FacilitiesFilter();
		if (StringUtils.isNotBlank(pagingRequest.getSearch().getValue())) {
			facilityFilter.setName(pagingRequest.getSearch().getValue());
		}
		Page<FacilityDto> page = facilitiesService.getFilteredPage(pagingRequest, facilityFilter);

		List<FacilityDto> data = page.getData();
		List<FacilityResponse> fasilitiesList = FacilityRespMapper.MAPPER.toRest(data);
		Page<FacilityResponse> pageRest = new Page<>(page, fasilitiesList);

		return new ResponseEntity<>(pageRest, HttpStatus.PARTIAL_CONTENT);

	}

	/**
	 * Gets the {@link FacilityRegistrationDto}.
	 *
	 * @param facilityId the facility id
	 * @return the {@link FacilityRegistrationDto}
	 */
	@GetMapping(value = "/{facilityId}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getFacilityById(@PathVariable(value = "facilityId") Long facilityId) {
		log.debug("IN getFacilityById - Get Facility by id {}", facilityId);

		// TODO return only Users approved for Editor
		Optional<FacilityDto> facilityDtoO = facilitiesService.fetchByIdDto(facilityId);

		if (facilityDtoO.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		FacilityResponse fasility = FacilityRespMapper.MAPPER.toRest(facilityDtoO.get());
		return ResponseEntity.ok(fasility);
	}

	/**
	 * Adds the new facility.
	 *
	 * @param newFacility the new facility
	 * @param loc         the loc
	 * @param principal   the principal
	 * @return the response entity
	 */
	@PostMapping(value = "", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> addNewFacility(
			@RequestBody(required = false) FacilityRegistrationRequest newFacility, Locale loc,
			Principal principal) {

		log.info("IN addNewFacility - Request register new Facility: '{}'", newFacility);

		if (newFacility == null) {
			log.warn("IN addNewFacility - Facility creation request must be not null");
			InfoResponse infoResponse = new InfoResponse(HttpStatus.BAD_REQUEST,
					i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST, loc),
					i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST_EXT, loc));

			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());
		}

		FacilityRegistrationDto facilityRegDto = ToFacilityRegistrationDtoMapper.MAPPER
				.fromFacilityRegistrationRequest(newFacility);

		log.info("IN addNewFacility - Perform register Facility");

		Optional<FacilityDto> facilityDtoO = facilitiesService.registerNewFacility(facilityRegDto);

		if (facilityDtoO.isEmpty()) { // Registration is fail
			log.info("IN addNewFacility - Registration is fail. Inform to the registrant");
			InfoResponse infoResponse = new InfoResponse(HttpStatus.INTERNAL_SERVER_ERROR,
					i18n.getTranslation(RestV1Msg.FACILITY_NEW_CHECK_FAIL, loc),
					i18n.getTranslation(RestV1Msg.FACILITY_NEW_CHECK_FAIL_EXT, loc));

			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());
		}

		log.info("IN addNewFacility - Registration is successful. Inform to the registrant");
		FacilityResponse facilityResponse = FacilityRespMapper.MAPPER.toRest(facilityDtoO.get());
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
	public ResponseEntity<Object> updateFacility(@PathVariable(value = "facilityId") Long facilityId,
			@RequestBody(required = false) FacilityUpdateRequest updatedFacility, Locale loc,
			Principal principal) {

		log.info("IN updateFacility - Request Facility update id='{}' by Facility data '{}'", facilityId,
				updatedFacility);
		if (updatedFacility == null) {
			log.warn("IN updateFacility - Facility update request must be not null");
			InfoResponse infoResponse = new InfoResponse(HttpStatus.BAD_REQUEST,
					i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST, loc),
					i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST_EXT, loc));

			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());
		}

		FacilityUpdateDto facilityUpdDto = ToFacilityUpdateDtoMapper.MAPPER
				.fromFacilityUpdateRequest(updatedFacility);

		facilityUpdDto.setId(facilityId);

		log.info("IN updateFacility - Perform update Facility");
		Optional<FacilityDto> facilityDtoO = facilitiesService.updateFacility(facilityUpdDto);

		if (facilityDtoO.isEmpty()) {
			log.info("IN updateFacility - Update is fail. Inform to the registrant");
			InfoResponse infoResponse = new InfoResponse(HttpStatus.INTERNAL_SERVER_ERROR,
					i18n.getTranslation(RestV1Msg.USER_UPDATE_FAIL, loc),
					i18n.getTranslation(RestV1Msg.USER_UPDATE_FAIL_EXT, loc));

			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());

		}

		log.info("IN updateFacility - Update is successful. Inform to the updater");
		FacilityResponse facilityResponse = FacilityRespMapper.MAPPER.toRest(facilityDtoO.get());
		return ResponseEntity.accepted().body(facilityResponse);
	}

	@DeleteMapping(value = "/{facilityId}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteFacility(@PathVariable(value = "facilityId") Long facilityId, Locale loc,
			Principal principal) {

		log.info("IN deleteFacility - Request Facility delete by id='{}'", facilityId);

		Optional<Facility> facilityO = facilitiesService.fetchById(facilityId);
		if (facilityO.isPresent()) {
			facilitiesService.delete(facilityO.get());
		}

		return null;
	}

	/**
	 * Adds the new facility addr.
	 *
	 * @param facilityId          the facility id
	 * @param facilityAddrDtoJson the facility addr dto json
	 * @param loc                 the loc
	 * @param principal           the principal
	 * @return the response entity
	 */
	@PostMapping(value = "/{id}" + AppURL.FACILITIESADDR, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addNewFacilityAddr(@PathVariable(value = "id") Long facilityId,
			@RequestBody String facilityAddrDtoJson, Locale loc, Principal principal) {

		ResponseEntity<Object> response = facilityAddrRestController.addNewFacilityAddr(facilityAddrDtoJson, facilityId,
				loc, principal);
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
	@GetMapping(value = "/{id}" + AppURL.FACILITIESADDR, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Object> getFacilityAddressesByFacilityId(@PathVariable(value = "id") Long facilityId,
			Locale loc, Principal principal) {
		// TODO return only Facilities approved for Editor
		Optional<FacilityDto> facilityDtoO = facilitiesService.fetchByIdDto(facilityId);
		if (facilityDtoO.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		List<FacilityAddressDto> response = facilityDtoO.get().getFacilityAddresses().stream()
				.collect(Collectors.toList());
		if (!response.isEmpty()) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.notFound().build();

	}

	/**
	 * Handle facility dto audit exeption.
	 *
	 * @param ex      the ex
	 * @param request the request
	 * @param loc     the loc
	 * @return the response entity
	 */
	@ExceptionHandler(value = { UserDtoAuditExeption.class })
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
		log.debug("IN handleOtherExceptions - Gets exception: {}", ex.getMessage());

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
}
