package ua.com.sipsoft.service.common;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.common.mapper.FacilityAddrDtoMapper;
import ua.com.sipsoft.service.dto.facility.FacilityAddrDto;
import ua.com.sipsoft.service.dto.facility.FacilityAddrRegReqDto;
import ua.com.sipsoft.service.dto.facility.FacilityAddrUpdReqDto;
import ua.com.sipsoft.service.exception.FacilityAddrDtoAuditExeption;
import ua.com.sipsoft.service.security.UserPrincipal;
import ua.com.sipsoft.service.util.audit.FacilityAddrCreateRequestDtoAuditor;
import ua.com.sipsoft.service.util.audit.FacilityAddrUpdReqDtoAuditor;
import ua.com.sipsoft.util.I18NProvider;
import ua.com.sipsoft.util.audit.AuditResponse;
import ua.com.sipsoft.util.message.RestV1Msg;
import ua.com.sipsoft.util.security.Role;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacilityAddrService {

	private final FacilityAddrCreateRequestDtoAuditor facilityAddrCreateDtoAuditor;
	private final FacilityAddrUpdReqDtoAuditor facilityAddrUpdReqDtoAuditor;
	private final I18NProvider i18n;
	private final FacilityAddrDtoMapper facilityAddressDtoMapper;

	private final FacilityAddrServiceToRepo facilityAddrServiceRepo;

	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER", "ROLE_PRODUCTOPER", "ROLE_COURIER",
			"ROLE_CLIENT" })
	public List<FacilityAddrDto> getAllFacilityAddrDto() {
		log.debug("getAllFacilityAddrDto] - Get all Facilities Adresses");
		User caller = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUser();
		List<FacilityAddrDto> facilityAddresses = List.of();
		if (caller.hasAnyRole(Role.ROLE_ADMIN,
				Role.ROLE_COURIER, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER,
				Role.ROLE_PRODUCTOPER)) {
			facilityAddresses = facilityAddrServiceRepo.getAllFacilityAddrDto();
		} else if (caller.hasAnyRole(Role.ROLE_CLIENT)) {
			facilityAddresses = facilityAddrServiceRepo.getAllFacilityAddrDto(caller.getId());
		}

		return facilityAddresses;
	}

	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER", "ROLE_PRODUCTOPER", "ROLE_COURIER",
			"ROLE_CLIENT" })
	public Optional<FacilityAddrDto> getFacilityAddrDtoById(@NonNull Long id) {
		log.debug("getFacilityAddrDtoById] - Get Facility Adress by id: '{}'", id);

		User caller = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUser();
		Optional<FacilityAddrDto> facilityAddressDtoO = Optional.empty();

		if (caller.hasAnyRole(Role.ROLE_ADMIN,
				Role.ROLE_COURIER, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER,
				Role.ROLE_PRODUCTOPER)) {
			facilityAddressDtoO = facilityAddrServiceRepo.fetchByIdDto(id);
		} else if (caller.hasAnyRole(Role.ROLE_CLIENT)) {
			facilityAddressDtoO = facilityAddrServiceRepo.fetchByIdDtoWithUser(id, caller);
		}

		return facilityAddressDtoO;
	}

	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER" })
	public Optional<FacilityAddrDto> registerNewFacilityAddr(@NonNull Long fasilityId,
			@NonNull FacilityAddrRegReqDto facilityAddrRegDto) {
		Locale locale = LocaleContextHolder.getLocale();

		trimSpaces(facilityAddrRegDto);

		AuditResponse response = facilityAddrCreateDtoAuditor.inspectNewData(facilityAddrRegDto, null, locale);

		if (response.isInvalid()) { // if there are any errors
			log.info("IN registerNewFacilityAddr - Registration is fail. Inform to the registrant");

			FacilityAddrDtoAuditExeption ex = new FacilityAddrDtoAuditExeption(response);
			ex.setErrMsg(i18n.getTranslation(RestV1Msg.FACILITY_NEW_ADDR_CHECK_FAIL, locale));
			ex.setErrMsgExt(i18n.getTranslation(RestV1Msg.FACILITY_NEW_ADDR_CHECK_FAIL_EXT, locale));
			throw ex;
		}

		FacilityAddrDto facilityAddressDto = facilityAddressDtoMapper.toDto(facilityAddrRegDto);

		return facilityAddrServiceRepo.registerNewFacilityAddress(fasilityId, facilityAddressDto);
	}

	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER" })
	public Optional<FacilityAddrDto> updateFacilityAddr(@NonNull Long fasilityAddrId,
			@NonNull FacilityAddrUpdReqDto facilityAddrUpdReqDto) {
		log.debug("updateFacilityAddr] - Try to update Facility address: '{}'", facilityAddrUpdReqDto);
		Locale locale = LocaleContextHolder.getLocale();

		trimSpaces(facilityAddrUpdReqDto);
		AuditResponse response = facilityAddrUpdReqDtoAuditor.inspectUpdatedData(facilityAddrUpdReqDto, null, locale);

		if (response.isInvalid()) {
			log.info("IN updateFacility - User update is fail. Inform to the updater");

			FacilityAddrDtoAuditExeption ex = new FacilityAddrDtoAuditExeption(response);
			ex.setErrMsg(i18n.getTranslation(RestV1Msg.FACILITYADDR_UPDATE_CHECK_FAIL, locale));
			ex.setErrMsgExt(i18n.getTranslation(RestV1Msg.FACILITYADDR_UPDATE_CHECK_FAIL_EXT, locale));
			throw ex;
		}

		return facilityAddrServiceRepo.updateFacilityAddress(fasilityAddrId, facilityAddrUpdReqDto);
	}

	/**
	 * @param facilityRegDto
	 */
	protected void trimSpaces(FacilityAddrRegReqDto facilityAddrRegDto) {
		if (facilityAddrRegDto == null) {
			return;
		}
		facilityAddrRegDto.setAddress(StringUtils.trim(facilityAddrRegDto.getAddress()));
		facilityAddrRegDto.setAddressesAlias(StringUtils.trim(facilityAddrRegDto.getAddressesAlias()));
		facilityAddrRegDto.setLat(StringUtils.trim(facilityAddrRegDto.getLat()));
		facilityAddrRegDto.setLng(StringUtils.trim(facilityAddrRegDto.getLng()));
	}

	protected void trimSpaces(FacilityAddrUpdReqDto facilityAddrRegDto) {
		if (facilityAddrRegDto == null) {
			return;
		}
		facilityAddrRegDto.setAddress(StringUtils.trim(facilityAddrRegDto.getAddress()));
		facilityAddrRegDto.setAddressesAlias(StringUtils.trim(facilityAddrRegDto.getAddressesAlias()));
		facilityAddrRegDto.setLat(StringUtils.trim(facilityAddrRegDto.getLat()));
		facilityAddrRegDto.setLng(StringUtils.trim(facilityAddrRegDto.getLng()));
	}

}
