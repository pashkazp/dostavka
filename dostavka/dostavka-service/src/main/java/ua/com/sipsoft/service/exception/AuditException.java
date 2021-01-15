package ua.com.sipsoft.service.exception;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

import lombok.Getter;
import lombok.Setter;
import ua.com.sipsoft.util.audit.AuditResponse;

@Getter
@Setter
public class AuditException extends CustomGenericException {

	private static final long serialVersionUID = 4728309790778116393L;

	private Multimap<String, String> auditMessages = TreeMultimap.create();

	public AuditException(AuditResponse audit) {
		audit.getMessages().forEach((key, value) -> auditMessages.put(key, value));
	}

	public void addAuditMessage(String key, String message) {
		auditMessages.put(key, message);
	}
}
