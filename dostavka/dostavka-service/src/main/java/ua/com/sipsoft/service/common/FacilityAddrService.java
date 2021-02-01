package ua.com.sipsoft.service.common;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.service.common.mapper.FacilityAddressDtoMapper;
import ua.com.sipsoft.service.dto.facility.FacilityAddrRegDto;
import ua.com.sipsoft.service.dto.facility.FacilityAddressDto;
import ua.com.sipsoft.service.exception.FacilityAddrDtoAuditExeption;
import ua.com.sipsoft.service.util.audit.FacilityAddrCreateRequestDtoAuditor;
import ua.com.sipsoft.util.I18NProvider;
import ua.com.sipsoft.util.audit.AuditResponse;
import ua.com.sipsoft.util.message.RestV1Msg;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacilityAddrService {

	private final FacilityAddrCreateRequestDtoAuditor facilityAddrCreateDtoAuditor;
	private final I18NProvider i18n;

	private final FacilityAddrServiceToRepo facilityAddrServiceRepo;
	private final FacilitiesServiceToRepo facilitiesServiceToRepo;

	public List<FacilityAddressDto> getAllFacilityAddrDto() {
		// TODO Auto-generated method stub
		return null;
	}

	public Optional<FacilityAddressDto> fetchByIdDto(@NonNull Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@RolesAllowed({ "ROLE_ADMIN", "ROLE_DISPATCHER", "ROLE_MANAGER" })
	public Optional<FacilityAddressDto> registerNewFacilityAddr(@NonNull Long fasilityId,
			FacilityAddrRegDto facilityAddrRegDto) {
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

		FacilityAddressDto facilityAddressDto = Mappers.getMapper(FacilityAddressDtoMapper.class)
				.toDto(facilityAddrRegDto);

		return facilityAddrServiceRepo.registerNewFacilityAddress(fasilityId, facilityAddressDto);
	}

	/**
	 * @param facilityRegDto
	 */
	protected void trimSpaces(FacilityAddrRegDto facilityAddrRegDto) {
		if (facilityAddrRegDto == null) {
			return;
		}
		facilityAddrRegDto.setAddress(StringUtils.trim(facilityAddrRegDto.getAddress()));
		facilityAddrRegDto.setAddressesAlias(StringUtils.trim(facilityAddrRegDto.getAddressesAlias()));
		facilityAddrRegDto.setLat(StringUtils.trim(facilityAddrRegDto.getLat()));
		facilityAddrRegDto.setLng(StringUtils.trim(facilityAddrRegDto.getLng()));
	}

}
