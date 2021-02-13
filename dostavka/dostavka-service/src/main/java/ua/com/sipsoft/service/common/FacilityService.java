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
import ua.com.sipsoft.service.dto.facility.FacilityDto;
import ua.com.sipsoft.service.dto.facility.FacilityRegReqDto;
import ua.com.sipsoft.service.dto.facility.FacilityUpdReqDto;
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
	private final FacilityAddrService facilityAddrService;
	private final I18NProvider i18n;
	private final FacilityCreateRequestDtoAuditor facilityCreateDtoAuditor;
	private final FacilityUpdateRequestDtoAuditor facilityUpdateDtoAuditor;

	public List<FacilityDto> getAllFacilitiesDto() {
		// TODO Auto-generated method stub
		return null;
	}

	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER", "ROLE_PRODUCTOPER", "ROLE_COURIER",
			"ROLE_CLIENT" })
	public Page<FacilityDto> getFilteredPage(@NonNull PagingRequest pagingRequest,
			@NonNull FacilitiesFilter facilityFilter) {

		facilityFilter.setCaller(((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUser());
		return facilityServiceRepo.getFilteredPage(pagingRequest, facilityFilter);
	}

	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER", "ROLE_PRODUCTOPER", "ROLE_COURIER",
			"ROLE_CLIENT" })
	public Optional<FacilityDto> getFacilityDtoById(@NonNull Long facilityId) {
		log.debug("getFacilityDtoById] - Get Facility by id: '{}'", facilityId);

		Optional<FacilityDto> facilityDtoO = facilityServiceRepo.fetchByIdDto(facilityId);

		if (facilityDtoO.isPresent()) {
			User caller = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
					.getUser();
			if (caller.hasAnyRole(Role.ROLE_ADMIN,
					Role.ROLE_COURIER, Role.ROLE_DISPATCHER, Role.ROLE_MANAGER,
					Role.ROLE_PRODUCTOPER)) {
				return facilityDtoO;
			} else if (caller.hasAnyRole(Role.ROLE_CLIENT) &&
					facilityDtoO.get().getUsers().stream().anyMatch(u -> u.getId() == caller.getId())) {
				return facilityDtoO;
			}
		}
		return Optional.empty();
	}

	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER" })
	public Optional<FacilityDto> registerNewFacility(@NonNull FacilityRegReqDto facilityRegDto) {
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
	public Optional<FacilityDto> updateFacility(@NonNull FacilityUpdReqDto facilityUpdDto) {
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
	public void deleteFacility(@NonNull Long facilityId) {

		facilityServiceRepo.delete(facilityId);
	}

	protected void trimSpaces(FacilityRegReqDto facilityRegDto) {
		if (facilityRegDto == null) {
			return;
		}
		facilityRegDto.setName(StringUtils.trim(facilityRegDto.getName()));
		if (facilityRegDto.getFacilityAddress() == null) {
			return;
		}
		facilityAddrService.trimSpaces(facilityRegDto.getFacilityAddress());
	}

	/**
	 * @param facilityUpdDto
	 */
	protected void trimSpaces(FacilityUpdReqDto facilityUpdDto) {
		if (facilityUpdDto == null) {
			return;
		}
		facilityUpdDto.setName(StringUtils.trim(facilityUpdDto.getName()));
	}

}
