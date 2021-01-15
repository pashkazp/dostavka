package ua.com.sipsoft.ui.util.audit;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.util.audit.AbstractAuditor;
import ua.com.sipsoft.util.audit.AuditResponse;

@Slf4j
public abstract class AbstractConfirmPasswordAuditor<T> implements AbstractAuditor<T> {

	public abstract String getPassword(T t);

	public abstract String getCPassword(T t);

	public abstract String getConfitmPasswordFieldName();

	public abstract String getPasswordFieldName();

	public abstract String getMismatchMessage(Locale loc);

	public abstract String getBlankPasswordMessage(Locale loc);

	public abstract String getBlankCPasswordMessage(Locale loc);

	@Override
	public AuditResponse validate(T audibleData, AuditResponse result, Locale loc) {
		if (result == null) {
			result = new AuditResponse();
		}

		// both is empty
		if (StringUtils.isBlank(getPassword(audibleData)) && StringUtils.isBlank(getCPassword(audibleData))) {
			result.setValid(true);
			return result;
		}

		// both not empty and not match
		if (StringUtils.isNotBlank(getPassword(audibleData)) && StringUtils.isNotBlank(getCPassword(audibleData))
				&& (StringUtils.compare(getPassword(audibleData), getCPassword(audibleData)) != 0)) {
			log.info("IN validate - Entered password and confirmPaasword is not match.");
			result.addMessage(getConfitmPasswordFieldName(), getMismatchMessage(loc));
			result.setValid(false);
			return result;
		}

		// only password empty
		if (StringUtils.isBlank(getPassword(audibleData))) {
			log.info("IN validate - Entered password is blank and confirm password is not blank.");
			result.addMessage(getPasswordFieldName(), getBlankPasswordMessage(loc));
			result.setValid(false);
			return result;
		}

		// only cPassword empty
		if (StringUtils.isBlank(getCPassword(audibleData))) {
			log.info("IN validate - Entered password is not blank and confirm password is blank.");
			result.addMessage(getConfitmPasswordFieldName(), getBlankCPasswordMessage(loc));
			result.setValid(false);
			return result;
		}

		result.setValid(true);
		return result;

	}

}
