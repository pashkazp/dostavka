package ua.com.sipsoft.util.audit;

import java.util.Locale;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public interface CreateRequestPropertyAuditor<T> {

	AuditResponse inspectNewData(@NonNull T inspectedInfo, AuditResponse result, Locale loc);

}
