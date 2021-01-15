package ua.com.sipsoft.util;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import lombok.Getter;;

/**
 *
 * @author Pavlo Degtyaryev
 */
public enum CourierVisitState {
	NEW("NEW", UIIcon.COURIERVISITSTATE_NEW),
	RUNNING("RUNNING", UIIcon.COURIERVISITSTATE_RUNNING),
	COMPLETED("COMPLETED", UIIcon.COURIERVISITSTATE_COMPLETED),
	CANCELLED("CANCELLED", UIIcon.COURIERVISITSTATE_CANCELLED);

	/**
	 * Gets the state name.
	 *
	 * @return the state name
	 */
	@Getter
	private String stateName;

	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	@Getter
	private final UIIcon icon;

	/**
	 * Instantiates a new courier visit state.
	 *
	 * @param stateName the state name
	 * @param icon      the icon
	 */
	private CourierVisitState(String stateName, UIIcon icon) {
		this.stateName = stateName;
		this.icon = icon;
	}

	/**
	 * Gets the class CSS.
	 *
	 * @return the class CSS
	 */
	public String getClassCSS() {
		return icon.getClassCSS();
	}

	/** The Constant INACTIVESET. */
	public static final Set<CourierVisitState> INACTIVESET = ImmutableSet.of(COMPLETED, CANCELLED);

	/** The Constant ACTIVESET. */
	public static final Set<CourierVisitState> ACTIVESET = ImmutableSet.of(NEW, RUNNING);
}