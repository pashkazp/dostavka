package ua.com.sipsoft.service.util.audit;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.service.dto.facility.FacilityRegReqDto;
import ua.com.sipsoft.util.I18NProvider;
import ua.com.sipsoft.util.audit.AuditResponse;
import ua.com.sipsoft.util.audit.CreateRequestPropertyAuditor;
import ua.com.sipsoft.util.message.FacilityEntityMsg;

@Component
@Slf4j
@RequiredArgsConstructor
public class FacilityCreateRequestDtoAuditor
		implements CreateRequestPropertyAuditor<FacilityRegReqDto> {

	private final I18NProvider i18n;
	private final static int facilityNameLengthMin = 1;
	private final static int facilityNameLengthMax = 200;
	private final FacilityAddrCreateRequestDtoAuditor addrAuditor;

	@Override
	public AuditResponse inspectNewData(FacilityRegReqDto inspectedInfo, AuditResponse result,
			@NonNull Locale loc) {
		if (result == null) {
			result = new AuditResponse();
			result.setValid(true);
		}
		if (inspectedInfo == null || StringUtils.length(inspectedInfo.getName()) < facilityNameLengthMin) {
			result.setValid(false);
			result.addMessage("name", i18n.getTranslation(FacilityEntityMsg.CHK_NAME_SHORT, loc));
			return result;
		}

		if (StringUtils.length(inspectedInfo.getName()) > facilityNameLengthMax) {
			result.setValid(false);
			result.addMessage("name", i18n.getTranslation(FacilityEntityMsg.CHK_NAME_LONG, loc));
			return result;
		}

		if (inspectedInfo.getFacilityAddress() != null && inspectedInfo.getFacilityAddress().isNotEmpty()) {
			result = addrAuditor.inspectNewData(inspectedInfo.getFacilityAddress(), result, loc);
		}

		return result;
	}

}
