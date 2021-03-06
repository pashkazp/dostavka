package ua.com.sipsoft.ui.util.audit;

import java.util.Locale;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.ui.model.request.user.UserUpdateRequest;
import ua.com.sipsoft.util.I18NProvider;
import ua.com.sipsoft.util.audit.AuditResponse;
import ua.com.sipsoft.util.audit.CreateRequestPropertyAuditor;
import ua.com.sipsoft.util.message.UserEntityCheckMsg;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserUpdateRequestConfirmPasswordAuditor extends AbstractConfirmPasswordAuditor<UserUpdateRequest>
		implements CreateRequestPropertyAuditor<UserUpdateRequest> {

	private final I18NProvider i18n;

	@Override
	public AuditResponse inspectNewData(UserUpdateRequest inspectedInfo, AuditResponse result,
			Locale loc) {
		return validate(inspectedInfo, result, loc);
	}

	@Override
	public String getPassword(UserUpdateRequest inspectedInfo) {
		return inspectedInfo.getPassword();
	}

	@Override
	public String getCPassword(UserUpdateRequest inspectedInfo) {
		return inspectedInfo.getConfirmPassword();
	}

	@Override
	public String getConfitmPasswordFieldName() {
		return "confirmPassword";
	}

	@Override
	public String getMismatchMessage(Locale loc) {
		return i18n.getTranslation(UserEntityCheckMsg.PASS_EQUAL, loc);
	}

	@Override
	public String getBlankPasswordMessage(Locale loc) {
		return i18n.getTranslation(UserEntityCheckMsg.PASS_EXPECT, loc);
	}

	@Override
	public String getBlankCPasswordMessage(Locale loc) {
		return i18n.getTranslation(UserEntityCheckMsg.CONFPASS_EXPECT, loc);
	}

	@Override
	public String getPasswordFieldName() {
		return "password";
	}

}
