package ua.com.sipsoft.util.audit;

import java.util.Locale;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public interface UpdateRequestPropertyAuditor<T> {

	AuditResponse inspectUpdatedData(@NonNull T inspectedInfo, AuditResponse result, Locale loc);

}
