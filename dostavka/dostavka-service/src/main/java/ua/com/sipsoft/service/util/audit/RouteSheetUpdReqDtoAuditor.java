package ua.com.sipsoft.service.util.audit;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.service.dto.request.DraftSheetUpdReqDto;
import ua.com.sipsoft.util.I18NProvider;
import ua.com.sipsoft.util.audit.AuditResponse;
import ua.com.sipsoft.util.audit.UpdateRequestPropertyAuditor;
import ua.com.sipsoft.util.message.DraftRouteSheetMsg;

@Component
@Slf4j
@RequiredArgsConstructor
public class RouteSheetUpdReqDtoAuditor
		implements UpdateRequestPropertyAuditor<DraftSheetUpdReqDto> {

	private final I18NProvider i18n;
	private final static int descriptionLengthMin = 1;
	private final static int descriptionLengthMax = 100;

	@Override
	public AuditResponse inspectUpdatedData(DraftSheetUpdReqDto inspectedInfo, AuditResponse result, Locale loc) {
		if (result == null) {
			result = new AuditResponse();
			result.setValid(true);
		}
		if (inspectedInfo == null || StringUtils.length(inspectedInfo.getDescription()) < descriptionLengthMin) {
			result.setValid(false);
			result.addMessage("name", i18n.getTranslation(DraftRouteSheetMsg.CHK_NAME_SHORT, loc));
			return result;
		}

		if (StringUtils.length(inspectedInfo.getDescription()) > descriptionLengthMax) {
			result.setValid(false);
			result.addMessage("name", i18n.getTranslation(DraftRouteSheetMsg.CHK_NAME_LONG, loc));
			return result;
		}
		return result;
	}

}
