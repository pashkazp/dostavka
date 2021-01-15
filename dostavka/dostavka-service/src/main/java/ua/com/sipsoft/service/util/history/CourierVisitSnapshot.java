package ua.com.sipsoft.service.util.history;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.com.sipsoft.dao.request.issued.CourierVisit;
import ua.com.sipsoft.dao.request.issued.CourierVisitEvent;
import ua.com.sipsoft.dao.request.prototype.AbstractCourierRequest;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.dao.util.CourierRequestSnapshot;
import ua.com.sipsoft.service.security.UsersUtils;
import ua.com.sipsoft.util.CourierVisitState;

/**
 *
 * @author Pavlo Degtyaryev
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CourierVisitSnapshot extends CourierRequestSnapshot {

	/** The state. */
	private CourierVisitState state;

	/**
	 * Make snapshot from {@link CourierVisit}.
	 *
	 * @param <T>          the generic type
	 * @param courierVisit the courier visit
	 */
	public <T extends AbstractCourierRequest> CourierVisitSnapshot(T courierVisit) {
		super(courierVisit);
		if (courierVisit instanceof CourierVisit) {
			this.state = ((CourierVisit) courierVisit).getState();
		}
	}

	/**
	 * Compare "Old state" of current snapshot and "New state" of newCourierVisit
	 * and register changes as events in {@link CourierVisitEvent}.
	 *
	 * @param <T>             the generic type
	 * @param newCourierVisit the new courier visit
	 * @param author          the author
	 */
	@Override
	public <T extends AbstractCourierRequest> void fillHistoryChangesTo(T newCourierVisit, User author) {
		super.fillHistoryChangesTo(newCourierVisit, author);
		if (newCourierVisit instanceof CourierVisit && this.state != ((CourierVisit) newCourierVisit).getState()) {
			newCourierVisit.addHistoryEvent(
					"Стан віклику був змінений з: " + this.state.name() + " на: "
							+ ((CourierVisit) newCourierVisit).getState().name(),
					LocalDateTime.now(), UsersUtils.getCurrentUser());
		}

	}
}