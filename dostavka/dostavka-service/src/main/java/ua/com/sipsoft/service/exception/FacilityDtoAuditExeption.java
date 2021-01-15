package ua.com.sipsoft.service.exception;

import ua.com.sipsoft.util.audit.AuditResponse;

public class FacilityDtoAuditExeption extends AuditException {

	private static final long serialVersionUID = -5182503405281302168L;

	public FacilityDtoAuditExeption(AuditResponse audit) {
		super(audit);
	}

}
