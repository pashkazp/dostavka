package ua.com.sipsoft.service.request.issued;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.defaultString;

import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.sipsoft.dao.request.issued.CourierVisit;
import ua.com.sipsoft.service.util.EntityFilter;
import ua.com.sipsoft.util.CourierVisitState;

/**
 * The Interface CourierVisitFilter.
 * 
 * @author Pavlo Degtyaryev
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CourierVisitFilter implements EntityFilter<CourierVisit> {

	/** The description. */
	@Builder.Default
	private String description = null;

	/** The courier visit state. */
	@Builder.Default
	private Set<CourierVisitState> courierVisitStates = null;

	@Override
	public String toString() {
		return String.format("CourierVisitFilter [description=\"%s\", courierVisitStates=%s]", description,
				courierVisitStates);
	}

	/**
	 * Checks if is pass.
	 *
	 * @param entity the entity
	 * @return true, if is pass
	 */
	@Override
	public boolean isPass(CourierVisit entity) {
		if (entity == null) {
			return false;
		}
		if (!containsIgnoreCase(entity.getDescription(), defaultString(description))) {
			return false;
		}
		if (courierVisitStates != null && !CollectionUtils.containsAny(courierVisitStates, entity.getState())) {
			return false;
		}

		return true;
	}

}