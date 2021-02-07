package ua.com.sipsoft.service.util.audit;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.service.dto.facility.FacilityAddrRegReqDto;
import ua.com.sipsoft.util.I18NProvider;
import ua.com.sipsoft.util.audit.AuditResponse;
import ua.com.sipsoft.util.audit.CreateRequestPropertyAuditor;
import ua.com.sipsoft.util.message.FacilityAddrEntityMsg;

/**
 * FacilityAddrCreateRequestDtoAuditor is a class for checking the properties of
 * a request to create a new Facility address.
 * 
 * @author Pavlo Degtyaryev
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class FacilityAddrCreateRequestDtoAuditor
		implements CreateRequestPropertyAuditor<FacilityAddrRegReqDto> {

	/** The i 18 n. */
	private final I18NProvider i18n;

	/** The Constant facilityAddrNameLengthMin is a minimum address length. */
	private final static int facilityAddrNameLengthMin = 1;

	/** The Constant facilityAddrNameLengthMax is a maximum address length. */
	private final static int facilityAddrNameLengthMax = 255;

	/**
	 * The Constant facilityAddrAliasLengthMax is a maximum address alias lendth.
	 */
	private final static int facilityAddrAliasLengthMax = 100;

	/** The Constant minLat is a minimum latitude. */
	private final static Double minLat = -90d;

	/** The Constant maxLat is a maximum latitude. */
	private final static Double maxLat = 90d;

	/** The Constant minLng is a minimum longitude. */
	private final static Double minLng = -180d;

	/** The Constant maxLng is a maximum longitude. */
	private final static Double maxLng = 180d;

	/**
	 * Inspect creation data.
	 *
	 * @param inspectedInfo the inspected info
	 * @param auditResponse the Audit response
	 * @param loc           the Locale
	 * @return the audit response
	 */
	@Override
	public AuditResponse inspectNewData(FacilityAddrRegReqDto inspectedInfo, AuditResponse auditResponse,
			@NonNull Locale loc) {
		if (auditResponse == null) {
			auditResponse = new AuditResponse();
			auditResponse.setValid(true);
		}

		if (inspectedInfo == null) {
			auditResponse.setValid(false);
			return auditResponse;
		}

		if (StringUtils.length(inspectedInfo.getAddress()) < facilityAddrNameLengthMin) {
			auditResponse.setValid(false);
			auditResponse.addMessage("address", i18n.getTranslation(FacilityAddrEntityMsg.CHK_ADDRESS_SHORT, loc));
		}
		if (StringUtils.length(inspectedInfo.getAddress()) > facilityAddrNameLengthMax) {
			auditResponse.setValid(false);
			auditResponse.addMessage("address", i18n.getTranslation(FacilityAddrEntityMsg.CHK_ADDRESS_LONG, loc));
		}

		if (StringUtils.length(inspectedInfo.getAddressesAlias()) > facilityAddrAliasLengthMax) {
			auditResponse.setValid(false);
			auditResponse.addMessage("addressesAlias", i18n.getTranslation(FacilityAddrEntityMsg.CHK_ALIAS_LONG, loc));
		}

		if (StringUtils.length(inspectedInfo.getLat()) > 0) {
			try {
				Double d = Double.parseDouble(inspectedInfo.getLat());
				if ((d < minLat) || (d > maxLat)) {
					auditResponse.setValid(false);
					auditResponse.addMessage("lat", i18n.getTranslation(FacilityAddrEntityMsg.CHK_GEO_RANGE, loc));
				}
			} catch (NumberFormatException e) {
				auditResponse.setValid(false);
				auditResponse.addMessage("lat", i18n.getTranslation(FacilityAddrEntityMsg.CHK_GEO_WRONG, loc));
			}
		}

		if (StringUtils.length(inspectedInfo.getLng()) > 0) {
			try {
				Double d = Double.parseDouble(inspectedInfo.getLng());
				if (d < minLng || d > maxLng) {
					auditResponse.setValid(false);
					auditResponse.addMessage("lng", i18n.getTranslation(FacilityAddrEntityMsg.CHK_GEO_RANGE, loc));
				}
			} catch (NumberFormatException e) {
				auditResponse.setValid(false);
				auditResponse.addMessage("lng", i18n.getTranslation(FacilityAddrEntityMsg.CHK_GEO_WRONG, loc));
			}
		}

		return auditResponse;
	}

}
