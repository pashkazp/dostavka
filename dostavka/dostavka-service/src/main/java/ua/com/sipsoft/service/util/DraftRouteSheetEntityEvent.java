package ua.com.sipsoft.service.util;

import org.apache.commons.lang3.SerializationUtils;

import lombok.Getter;
import ua.com.sipsoft.dao.request.draft.DraftRouteSheet;

public class DraftRouteSheetEntityEvent {
	@Getter
	final private DraftRouteSheet data;

	@Getter
	final private EntityOperationType operationType;

	public DraftRouteSheetEntityEvent(EntityOperationType operationType, DraftRouteSheet data) {
		this.operationType = operationType;
		this.data = SerializationUtils.clone(data);
	}

	@Override
	public String toString() {
		return String.format("DraftRouteSheetEntityEvent [data=%s, operationType=%s]", data, operationType);
	}

}