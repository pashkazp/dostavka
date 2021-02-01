package ua.com.sipsoft.service.exception;

import ua.com.sipsoft.util.audit.AuditResponse;

public class FacilityAddrDtoAuditExeption extends AuditException {

	private static final long serialVersionUID = -5716722215063081565L;

	public FacilityAddrDtoAuditExeption(AuditResponse audit) {
		super(audit);
	}

}
