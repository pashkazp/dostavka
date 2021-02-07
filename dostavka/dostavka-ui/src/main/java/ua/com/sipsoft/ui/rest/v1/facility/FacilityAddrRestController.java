package ua.com.sipsoft.ui.rest.v1.facility;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.collect.Multimap;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.service.common.FacilityAddrService;
import ua.com.sipsoft.service.dto.facility.FacilityAddrDto;
import ua.com.sipsoft.service.dto.facility.FacilityAddrRegReqDto;
import ua.com.sipsoft.service.dto.facility.FacilityAddrUpdReqDto;
import ua.com.sipsoft.service.exception.FacilityAddrDtoAuditExeption;
import ua.com.sipsoft.service.exception.FacilityAddrNotFoundException;
import ua.com.sipsoft.service.exception.FacilityNotFoundException;
import ua.com.sipsoft.service.exception.ResourceNotFoundException;
import ua.com.sipsoft.service.util.audit.LatLngAuditor;
import ua.com.sipsoft.ui.model.request.facility.FacilityAddrRegReq;
import ua.com.sipsoft.ui.model.request.facility.FacilityAddrUpdReq;
import ua.com.sipsoft.ui.model.request.mapper.ToFacilityAddrRegDtoMapper;
import ua.com.sipsoft.ui.model.request.mapper.ToFacilityAddrUpdDtoMapper;
import ua.com.sipsoft.ui.model.response.AbstractSubInfoResponse;
import ua.com.sipsoft.ui.model.response.InfoResponse;
import ua.com.sipsoft.ui.model.response.ValidationInfoResponse;
import ua.com.sipsoft.ui.model.response.facility.FacilityAddrResponse;
import ua.com.sipsoft.ui.model.response.mapper.FacilityAddrRespMapper;
import ua.com.sipsoft.util.AppURL;
import ua.com.sipsoft.util.I18NProvider;
import ua.com.sipsoft.util.message.RestV1Msg;

@Slf4j
@RestController
@RequestMapping(AppURL.API_V1_FACILITIESADDR)
@RequiredArgsConstructor
public class FacilityAddrRestController implements LatLngAuditor {

	private final FacilityAddrService facilityAddrService;
	/** The i18n provider. */
	private final I18NProvider i18n;
	private final ToFacilityAddrRegDtoMapper toFacilityAddrRegDtoMapper;
	private final ToFacilityAddrUpdDtoMapper toFacilityAddrUpdDtoMapper;
	private final FacilityAddrRespMapper facilityAddrRespMapper;

	@GetMapping(value = "", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@Operation(description = "Retrieves a list of Faility addresses")
	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER", "ROLE_PRODUCTOPER", "ROLE_COURIER",
			"ROLE_CLIENT" })
	public ResponseEntity<Object> listAllFacilitiesAddrDto() {

		log.debug("Get the list of  all FacilityAddrDto");

		List<FacilityAddrDto> facilitiesAddrDto = facilityAddrService.getAllFacilityAddrDto();
		if (!facilitiesAddrDto.isEmpty()) {

			return ResponseEntity.ok(facilityAddrRespMapper.toRest(facilitiesAddrDto));
		}

		return ResponseEntity.notFound().build();
	}

	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@Operation(description = "Retrieves a single Faility address")
	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER", "ROLE_PRODUCTOPER", "ROLE_COURIER",
			"ROLE_CLIENT" })
	public ResponseEntity<Object> getFacilityDto(@PathVariable(value = "id") Long id) {

		log.debug("getFacilityDto] - Get Facility Address by id {}", id);

		Optional<FacilityAddrDto> facilityAddrDtoO = facilityAddrService.getFacilityAddrDtoById(id);

		if (facilityAddrDtoO.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		FacilityAddrResponse fasilityAddr = facilityAddrRespMapper.toRest(facilityAddrDtoO.get());
		return ResponseEntity.ok(fasilityAddr);
	}

	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER" })
	protected ResponseEntity<Object> addNewFacilityAddr(Long fasilityId,
			FacilityAddrRegReq newFacilityAddrReq) {

		log.info("addNewFacilityAddr] - Request register by FacilityId '{}' new Facility Address '{}'", fasilityId,
				newFacilityAddrReq);

		Locale loc = LocaleContextHolder.getLocale();

		if (newFacilityAddrReq == null || fasilityId == null) {
			log.warn("addNewFacilityAddr] - Facility address creation request or Facility Id must be not null");
			InfoResponse infoResponse = new InfoResponse(HttpStatus.BAD_REQUEST,
					i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST, loc),
					i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST_EXT, loc));

			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());
		}

		FacilityAddrRegReqDto facilityAddrRegDto = toFacilityAddrRegDtoMapper
				.fromFacilityRegReq(newFacilityAddrReq);

		log.info("addNewFacilityAddr] - Perform register Facility address");

		Optional<FacilityAddrDto> facilityAddrDtoO = facilityAddrService.registerNewFacilityAddr(fasilityId,
				facilityAddrRegDto);

		if (facilityAddrDtoO.isEmpty()) { // Registration is fail
			log.info("addNewFacilityAddr] - Registration is fail. Inform to the registrant");
			InfoResponse infoResponse = new InfoResponse(HttpStatus.INTERNAL_SERVER_ERROR,
					i18n.getTranslation(RestV1Msg.INFO_INACESSIBLE, loc),
					i18n.getTranslation(RestV1Msg.INFO_INACESSIBLE_EXT, loc));

			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());
		}

		log.info("addNewFacilityAddr] - Registration is successful. Inform to the registrant");

		FacilityAddrResponse facilityAddrResponse = facilityAddrRespMapper.toRest(facilityAddrDtoO.get());

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path(AppURL.API_V1_FACILITIESADDR)
				.path("/{id}")
				.buildAndExpand(facilityAddrResponse.getId()).toUri();

		return ResponseEntity.created(location).body(facilityAddrResponse);

	}

	@PutMapping(value = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER" })
	public ResponseEntity<Object> updateFacilityAddr(@PathVariable(value = "id") Long fasilityAddrId,
			@RequestBody(required = false) FacilityAddrUpdReq facilityAddrUpdReq, Locale locale) {
		log.debug("updateFacilityAddr] - try to  update new address: {}", facilityAddrUpdReq);
		if (facilityAddrUpdReq == null) {
			log.warn("updateFacilityAddr] - Facility address update request must be not null");
			InfoResponse infoResponse = new InfoResponse(HttpStatus.BAD_REQUEST,
					i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST, locale),
					i18n.getTranslation(RestV1Msg.ERR_BAD_REQUEST_EXT, locale));

			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());
		}

		FacilityAddrUpdReqDto facilityAddrUpdReqDto = toFacilityAddrUpdDtoMapper
				.fromFacilityAddrUpdReq(facilityAddrUpdReq);
		log.info("updateFacilityAddr] - Perform update Facility address");
		Optional<FacilityAddrDto> facilityAddrDtoO = facilityAddrService.updateFacilityAddr(fasilityAddrId,
				facilityAddrUpdReqDto);
		if (facilityAddrDtoO.isEmpty()) {
			log.info("updateFacilityAddr] - Update is fail. Inform to the registrant");
			InfoResponse infoResponse = new InfoResponse(HttpStatus.INTERNAL_SERVER_ERROR,
					i18n.getTranslation(RestV1Msg.FACILITYADDR_UPDATE_FAIL, locale),
					i18n.getTranslation(RestV1Msg.FACILITYADDR_UPDATE_FAIL_EXT, locale));

			return new ResponseEntity<>(infoResponse, infoResponse.getStatus());
		}

		log.info("updateFacilityAddr] - Update is successful. Inform to the updater");

		FacilityAddrResponse facilityAddrResponse = facilityAddrRespMapper.toRest(facilityAddrDtoO.get());
		return ResponseEntity.ok().body(facilityAddrResponse);

	}

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

	/**
	 * Handle facility address dto audit exeption.
	 *
	 * @param ex      the Exception
	 * @param request the request
	 * @param loc     the Locale
	 * @return the response entity
	 */
	@ExceptionHandler(value = { FacilityAddrDtoAuditExeption.class })
	@ResponseBody()
	public ResponseEntity<Object> handleFacilityDtoAuditExeption(FacilityAddrDtoAuditExeption ex, WebRequest request,
			Locale loc) {
		log.debug("handleFacilityDtoAuditExeption] - Gets exception: {}", ex.getMessage());

		InfoResponse infoResponse = new InfoResponse(HttpStatus.BAD_REQUEST, ex.getErrMsg(), ex.getErrMsgExt());

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

	@ExceptionHandler(value = { ResourceNotFoundException.class })
	@ResponseBody()
	public ResponseEntity<Object> handleFacilityAddrNotFoundException(ResourceNotFoundException ex,
			WebRequest request,
			Locale loc) {
		log.debug("handleFacilityAddrNotFoundException] - Gets exception: {}", ex.getMessage());

		InfoResponse infoResponse = new InfoResponse(HttpStatus.BAD_REQUEST,
				i18n.getTranslation(RestV1Msg.FACILITYADDR_NOTFOUND, loc),
				i18n.getTranslation(RestV1Msg.FACILITYADDR_NOTFOUND_EXT, loc));

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

	@ExceptionHandler(value = { FacilityAddrNotFoundException.class })
	@ResponseBody()
	public ResponseEntity<Object> handleFacilityAddrNotFoundException(FacilityAddrNotFoundException ex,
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
