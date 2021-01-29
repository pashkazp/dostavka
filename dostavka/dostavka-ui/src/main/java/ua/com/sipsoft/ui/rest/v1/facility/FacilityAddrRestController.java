package ua.com.sipsoft.ui.rest.v1.facility;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.DoubleValidator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.service.common.FacilityAddrServiceToRepo;
import ua.com.sipsoft.service.dto.facility.FacilityAddressDto;
import ua.com.sipsoft.service.util.audit.LatLngAuditor;
import ua.com.sipsoft.util.AppURL;
import ua.com.sipsoft.util.I18NProvider;
import ua.com.sipsoft.util.audit.AuditResponse;
import ua.com.sipsoft.util.message.AppNotifyMsg;
import ua.com.sipsoft.util.message.FacilityAddrEntityMsg;

@Slf4j
@RestController
@RequestMapping(AppURL.API_V1_FACILITIESADDR)
@RequiredArgsConstructor
public class FacilityAddrRestController implements LatLngAuditor {

	private final FacilityAddrServiceToRepo facilityAddrService;
	/** The i18n provider. */
	private final I18NProvider i18n;

	private final static int facilityAddAliasLengthMax = 100;

	private final static int facilityAddLengthMin = 1;
	private final static int facilityAddLengthMax = 255;

	private final ObjectMapper objectMapper;

	@Operation(description = "Retrieves a list of Faility addresses")
	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listAllFacilitiesAddrDto() {
		log.debug("Get the list of  all FacilityAddrDto");
		// TODO return only Facilities Adresses approved for Editor
		List<FacilityAddressDto> facilitiesAddrDto = facilityAddrService.getAllFacilityAddrDto();
		if (!facilitiesAddrDto.isEmpty()) {
			return ResponseEntity.ok(facilitiesAddrDto);
		}
		return ResponseEntity.notFound().build();
	}

	@Operation(description = "Retrieves a single Faility address")
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FacilityAddressDto> getFacilityDto(@PathVariable(value = "id") Long id) {
		log.debug("Get Facility Address by id {}", id);
		// TODO return only Users approved for Editor
		Optional<FacilityAddressDto> facilityAddrDtoO = facilityAddrService.fetchByIdDto(id);
		return ResponseEntity.of(facilityAddrDtoO);
	}

	private AuditResponse performUpdateFacilityAddrCheck(AuditResponse result,
			FacilityAddressDto facilityAddrDto,
			Locale loc) {
		result = performFacilityAddrBaseCheck(result, facilityAddrDto, loc);
		return result;
	}

	private AuditResponse performNewFacilityAddrCheck(AuditResponse result,
			FacilityAddressDto facilityAddrDto,
			Locale loc) {
		result = performFacilityAddrBaseCheck(result, facilityAddrDto, loc);
		return result;
	}

	private AuditResponse performFacilityAddrBaseCheck(AuditResponse result,
			FacilityAddressDto facilityAddrDto, Locale loc) {

		if (facilityAddrDto.getAddressesAlias() != null
				&& facilityAddrDto.getAddressesAlias().length() > facilityAddAliasLengthMax) {
			result.setValid(false);
			result.addMessage("addressesAlias", i18n.getTranslation(FacilityAddrEntityMsg.CHK_ALIAS_LONG, loc));
		}

		if (StringUtils.isBlank(facilityAddrDto.getAddress())
				|| facilityAddrDto.getAddress().length() < facilityAddLengthMin) {
			result.setValid(false);
			result.addMessage("address", i18n.getTranslation(FacilityAddrEntityMsg.CHK_ADDRESS_SHORT, loc));
		}

		if (facilityAddrDto.getAddress() != null && facilityAddrDto.getAddress().length() > facilityAddLengthMax) {
			result.setValid(false);
			result.addMessage("address", i18n.getTranslation(FacilityAddrEntityMsg.CHK_ADDRESS_LONG, loc));
		}

		if (!isLatitudeAgreed(facilityAddrDto.getLat(), true)) {
			result.setValid(false);
			result.addMessage("lat", i18n.getTranslation(FacilityAddrEntityMsg.CHK_GEO_RANGE, loc));
		}

		if (!isLongitudeAgreed(facilityAddrDto.getLng(), true)) {
			result.setValid(false);
			result.addMessage("lng", i18n.getTranslation(FacilityAddrEntityMsg.CHK_GEO_RANGE, loc));
		}

		return result;
	}

	protected ResponseEntity<Object> addNewFacilityAddr(String facilityAddrDtoJson,
			Long fasilityId, Locale loc, Principal principal) {

		log.info("Request register new Facility Address '{}'", facilityAddrDtoJson);
		Optional<FacilityAddressDto> facilityAddrDto;
		AuditResponse response = new AuditResponse();
		try {
			JsonNode rootNode = objectMapper.readTree(facilityAddrDtoJson);
			JsonNode facilityAddrNode = rootNode.findValue("facilityAddress");

			facilityAddrDto = facilityAddrDtoNodeToDto(facilityAddrNode, loc, response);
		} catch (NullPointerException | JsonProcessingException e) {
			response.setValid(false);
			response.addMessage("", i18n.getTranslation(AppNotifyMsg.FACILITYADDR_NOT_ADDED, loc));
			return ResponseEntity.badRequest().body(response);
		}

		if (facilityAddrDto.isEmpty()
				|| (facilityAddrDto.get().getId() != null && facilityAddrDto.get().getId() != fasilityId)) {
			response.setValid(false);
			response.addMessage("", i18n.getTranslation(AppNotifyMsg.FACILITYADDR_NOT_ADDED, loc));
			return ResponseEntity.badRequest().body(response);
		}

		response = performNewFacilityAddrCheck(response, facilityAddrDto.get(), loc);

		response.setValid(response.getMessages().isEmpty());

		if (response.isInvalid()) {
			return ResponseEntity.badRequest().body(response);
		}

		log.info("Perform register new Facility Address");
		facilityAddrDto = facilityAddrService.registerNewFacilityAddress(facilityAddrDto.get(),
				fasilityId);

		if (facilityAddrDto.isPresent()) {
			log.info("Registration is successful. Inform to the registrant");
			try {
				return ResponseEntity
						.created(new URI(AppURL.API_V1_FACILITIESADDR + "/" + facilityAddrDto.get().getId()))
						.body(facilityAddrDto.get());
			} catch (URISyntaxException e) {
				// e.printStackTrace();
			}
		}
		log.info("Registration new Fasility Address is failed. Inform to registrant.");
		response.setValid(false);
		response.addMessage("", i18n.getTranslation(AppNotifyMsg.FACILITYADDR_NOT_ADDED, loc));

		return ResponseEntity.badRequest().body(response);
	}

	public Optional<FacilityAddressDto> facilityAddrDtoNodeToDto(JsonNode facilityAddrNode, Locale loc,
			AuditResponse response) {
		log.info("Perform convert Facility Address Dto Node to Facility Address Dto'{}'", facilityAddrNode);

		if (facilityAddrNode == null || loc == null) {
			return Optional.empty();
		}
		if (response == null) {
			response = new AuditResponse();
		}
		FacilityAddressDto facilityAddrDto;

		JsonNode addressesAliasNode = facilityAddrNode.get("addressesAlias");
		JsonNode addressNode = facilityAddrNode.get("address");
		JsonNode latNode = facilityAddrNode.get("lat");
		JsonNode lngNode = facilityAddrNode.get("lng");
		JsonNode defaultAddressNode = facilityAddrNode.get("defaultAddress");

		if (addressesAliasNode == null || addressesAliasNode.isNull() ||
				addressNode == null || addressNode.isNull() ||
				latNode == null || latNode.isNull() ||
				lngNode == null || lngNode.isNull() ||
				defaultAddressNode == null || defaultAddressNode.isNull()) {
			response.setValid(false);
			response.addMessage("", i18n.getTranslation(AppNotifyMsg.FACILITYADDR_NOT_ADDED, loc));
			return Optional.empty();
		}

		String coordinate = latNode.asText();
		if (StringUtils.isNotBlank(coordinate)) {
			Double d = DoubleValidator.getInstance().validate(coordinate, loc);
			if (d != null && isLatitudeAgreed(d, false)) {
				((ObjectNode) facilityAddrNode).put("lat", d);
			} else {
				response.setValid(false);
				response.addMessage("", i18n.getTranslation(FacilityAddrEntityMsg.CHK_GEO_WRONG, loc));
				return Optional.empty();
			}
		}

		coordinate = lngNode.asText();
		if (StringUtils.isNotBlank(coordinate)) {
			Double d = DoubleValidator.getInstance().validate(coordinate, loc);
			if (d != null && isLongitudeAgreed(d, false)) {
				((ObjectNode) facilityAddrNode).put("lng", d);
			} else {
				response.setValid(false);
				response.addMessage("lng", i18n.getTranslation(FacilityAddrEntityMsg.CHK_GEO_WRONG, loc));
				return Optional.empty();
			}
		}

		try {
			facilityAddrDto = objectMapper.treeToValue(facilityAddrNode, FacilityAddressDto.class);
		} catch (JsonProcessingException e) {
			// e.printStackTrace();
			response.setValid(false);
			response.addMessage("lng", i18n.getTranslation(AppNotifyMsg.FACILITYADDR_NOT_ADDED, loc));
			return Optional.empty();

		}
		return Optional.of(facilityAddrDto);
	}
}
