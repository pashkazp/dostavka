package ua.com.sipsoft.service.common;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.dto.facility.FacilityDto;
import ua.com.sipsoft.service.dto.facility.FacilityRegistrationDto;
import ua.com.sipsoft.service.dto.facility.FacilityUpdateDto;
import ua.com.sipsoft.service.exception.FacilityDtoAuditExeption;
import ua.com.sipsoft.service.security.UserPrincipal;
import ua.com.sipsoft.service.util.audit.FacilityCreateRequestDtoAuditor;
import ua.com.sipsoft.service.util.audit.FacilityUpdateRequestDtoAuditor;
import ua.com.sipsoft.util.I18NProvider;
import ua.com.sipsoft.util.audit.AuditResponse;
import ua.com.sipsoft.util.message.RestV1Msg;
import ua.com.sipsoft.util.paging.Page;
import ua.com.sipsoft.util.paging.PagingRequest;
import ua.com.sipsoft.util.security.Role;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacilityService {

	private final FacilitiesServiceToRepo facilityServiceRepo;
	private final I18NProvider i18n;
	private final FacilityCreateRequestDtoAuditor facilityCreateDtoAuditor;
	private final FacilityUpdateRequestDtoAuditor facilityUpdateDtoAuditor;

	public List<FacilityDto> getAllFacilitiesDto() {
		// TODO Auto-generated method stub
		return null;
	}

	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER", "ROLE_PRODUCTOPER", "ROLE_COURIER",
			"ROLE_CLIENT" })
	public Page<FacilityDto> getFilteredPage(PagingRequest pagingRequest, FacilitiesFilter facilityFilter) {

		facilityFilter.setCaller(((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUser());
		return facilityServiceRepo.getFilteredPage(pagingRequest, facilityFilter);
	}

	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER", "ROLE_PRODUCTOPER", "ROLE_COURIER",
			"ROLE_CLIENT" })
	public Optional<FacilityDto> fetchByIdDto(Long facilityId) {
		log.debug("fetchByIdDto] - Get Facility by id: '{}'", facilityId);

		Optional<FacilityDto> facilityDtoO = facilityServiceRepo.fetchByIdDto(facilityId);

		if (facilityDtoO.isPresent()) {
			User caller = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
					.getUser();
			if (caller.hasRoles(Role.ROLE_ADMIN,
					Role.ROLE_COURIER, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER,
					Role.ROLE_PRODUCTOPER)) {
				return facilityDtoO;
			} else if (caller.hasRoles(Role.ROLE_CLIENT) &&
					CollectionUtils.containsAny(facilityDtoO.get().getUsers(), caller)) {
				return facilityDtoO;
			}
		}
		return Optional.empty();
	}

	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER" })
	public Optional<FacilityDto> registerNewFacility(@NonNull FacilityRegistrationDto facilityRegDto) {
		log.debug("registerNewFacility] - Try to register new Facility: '{}'", facilityRegDto);

		Locale loc = LocaleContextHolder.getLocale();

		trimSpaces(facilityRegDto);

		AuditResponse response = facilityCreateDtoAuditor.inspectNewData(facilityRegDto, null, loc);

		if (response.isInvalid()) { // if there are any errors
			log.info("IN registerNewFacility - Registration is fail. Inform to the registrant");

			FacilityDtoAuditExeption ex = new FacilityDtoAuditExeption(response);
			ex.setErrMsg(i18n.getTranslation(RestV1Msg.FACILITY_NEW_CHECK_FAIL, loc));
			ex.setErrMsgExt(i18n.getTranslation(RestV1Msg.FACILITY_NEW_CHECK_FAIL_EXT, loc));
			throw ex;
		}
		return facilityServiceRepo.registerNewFacility(facilityRegDto);
	}

	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER" })
	public Optional<FacilityDto> updateFacility(FacilityUpdateDto facilityUpdDto) {
		log.debug("updateFacility] - Try to update Facility: '{}'", facilityUpdDto);
		Locale loc = LocaleContextHolder.getLocale();

		trimSpaces(facilityUpdDto);

		AuditResponse response = facilityUpdateDtoAuditor.inspectUpdatedData(facilityUpdDto, null, loc);

		if (response.isInvalid()) {
			log.info("IN updateFacility - User update is fail. Inform to the updater");

			FacilityDtoAuditExeption ex = new FacilityDtoAuditExeption(response);
			ex.setErrMsg(i18n.getTranslation(RestV1Msg.FACILITY_UPDATE_CHECK_FAIL, loc));
			ex.setErrMsgExt(i18n.getTranslation(RestV1Msg.FACILITY_UPDATE_CHECK_FAIL_EXT, loc));
			throw ex;
		}

		return facilityServiceRepo.updateFacility(facilityUpdDto);
	}

	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER" })
	public void deleteFacility(Long facilityId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || auth.getPrincipal() == null) {
			log.debug("fetchByIdDto] - Get Facility by id is impossible. Authentication is null.");
			return;
		}

		Locale loc = LocaleContextHolder.getLocale();

		User caller = ((UserPrincipal) auth.getPrincipal()).getUser();
		if (caller.hasNoOneRole(Role.ROLE_ADMIN, Role.ROLE_DISPATCHER)) {
			throw new AccessDeniedException(i18n.getTranslation(RestV1Msg.ACCESS_DENIED, loc));
		}

		facilityServiceRepo.delete(facilityId);
	}

	/**
	 * @param facilityRegDto
	 */
	private void trimSpaces(FacilityRegistrationDto facilityRegDto) {
		facilityRegDto.setName(StringUtils.trim(facilityRegDto.getName()));
		facilityRegDto.getFacilityAddress()
				.setAddress(StringUtils.trim(facilityRegDto.getFacilityAddress().getAddress()));
		facilityRegDto.getFacilityAddress()
				.setAddressesAlias(StringUtils.trim(facilityRegDto.getFacilityAddress().getAddressesAlias()));
		facilityRegDto.getFacilityAddress().setLat(StringUtils.trim(facilityRegDto.getFacilityAddress().getLat()));
		facilityRegDto.getFacilityAddress().setLng(StringUtils.trim(facilityRegDto.getFacilityAddress().getLng()));
	}

	/**
	 * @param facilityUpdDto
	 */
	private void trimSpaces(FacilityUpdateDto facilityUpdDto) {
		facilityUpdDto.setName(StringUtils.trim(facilityUpdDto.getName()));
	}

}
