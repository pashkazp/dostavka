package ua.com.sipsoft.service.exception;

import ua.com.sipsoft.util.audit.AuditResponse;

public class UserDtoAuditExeption extends AuditException {

	private static final long serialVersionUID = -516724365011829226L;

	public UserDtoAuditExeption(AuditResponse audit) {
		super(audit);
	}

}
