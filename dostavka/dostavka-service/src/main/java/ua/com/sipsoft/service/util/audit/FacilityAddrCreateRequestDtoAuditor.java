package ua.com.sipsoft.service.util.audit;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.service.dto.facility.FacilityAddrRegDto;
import ua.com.sipsoft.util.I18NProvider;
import ua.com.sipsoft.util.audit.AuditResponse;
import ua.com.sipsoft.util.audit.CreateRequestPropertyAuditor;
import ua.com.sipsoft.util.message.FacilityAddrEntityMsg;

@Component
@Slf4j
@RequiredArgsConstructor
public class FacilityAddrCreateRequestDtoAuditor
		implements CreateRequestPropertyAuditor<FacilityAddrRegDto> {

	private final I18NProvider i18n;
	private final static int facilityAddrNameLengthMin = 1;
	private final static int facilityAddrNameLengthMax = 255;
	private final static int facilityAddrAliasLengthMax = 100;
	private final static Double minLat = -90d;
	private final static Double maxLat = 90d;
	private final static Double minLng = -180d;
	private final static Double maxLng = 180d;

	@Override
	public AuditResponse inspectNewData(FacilityAddrRegDto inspectedInfo, AuditResponse result,
			Locale loc) {
		if (result == null) {
			result = new AuditResponse();
			result.setValid(true);
		}

		if (inspectedInfo == null) {
			return result;
		}

		if (StringUtils.length(inspectedInfo.getAddress()) < facilityAddrNameLengthMin
				&& (StringUtils.length(inspectedInfo.getAddress()) > 0
						|| StringUtils.length(inspectedInfo.getAddressesAlias()) > 0
						|| StringUtils.length(inspectedInfo.getLat()) > 0
						|| StringUtils.length(inspectedInfo.getLng()) > 0)) {
			result.setValid(false);
			result.addMessage("address", i18n.getTranslation(FacilityAddrEntityMsg.CHK_ADDRESS_SHORT, loc));
		}
		if (StringUtils.length(inspectedInfo.getAddress()) > facilityAddrNameLengthMax) {
			result.setValid(false);
			result.addMessage("address", i18n.getTranslation(FacilityAddrEntityMsg.CHK_ADDRESS_LONG, loc));
		}

		if (StringUtils.length(inspectedInfo.getAddressesAlias()) > facilityAddrAliasLengthMax) {
			result.setValid(false);
			result.addMessage("addressesAlias", i18n.getTranslation(FacilityAddrEntityMsg.CHK_ALIAS_LONG, loc));
		}

		if (StringUtils.length(inspectedInfo.getLat()) > 0) {
			try {
				Double d = Double.parseDouble(inspectedInfo.getLat());
				if (d < minLat || d > maxLat) {
					result.setValid(false);
					result.addMessage("lat", i18n.getTranslation(FacilityAddrEntityMsg.CHK_GEO_RANGE, loc));
				}
			} catch (NumberFormatException e) {
				result.setValid(false);
				result.addMessage("lat", i18n.getTranslation(FacilityAddrEntityMsg.CHK_GEO_WRONG, loc));
			}
		}

		if (StringUtils.length(inspectedInfo.getLng()) > 0) {
			try {
				Double d = Double.parseDouble(inspectedInfo.getLng());
				if (d < minLng || d > maxLng) {
					result.setValid(false);
					result.addMessage("lng", i18n.getTranslation(FacilityAddrEntityMsg.CHK_GEO_RANGE, loc));
				}
			} catch (NumberFormatException e) {
				result.setValid(false);
				result.addMessage("lng", i18n.getTranslation(FacilityAddrEntityMsg.CHK_GEO_WRONG, loc));
			}
		}

		return result;
	}
}
