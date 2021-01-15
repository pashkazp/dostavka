package ua.com.sipsoft.util.audit;

import java.util.Locale;

public interface AbstractAuditor<T> {

	public abstract AuditResponse validate(T audibleData, AuditResponse result, Locale loc);

}
