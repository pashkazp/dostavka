package ua.com.sipsoft.util;

/**
 * Standartized set of icons for application.
 *
 * @author Pavlo Degtyaryev
 */
public enum UIIcon {
	HOME("fas fa-home"),
	USERS("fas fa-user-friends"),
	GROUP("fas fa-users"),
	BELL("fas fa-bell"),
	COURIER_RQUESTS("fas fa-list-ol"),
	SIGN_OUT("fas fa-sign-out-alt"),
	AUTHOR("fas fa-child"),
	CALENDAR("fas fa-calendar-alt"),
	MAP_MARKER("fas fa-map-marked-alt"),
	ADDRESS("fas fa-road"),
	THEME_PAINTER("fas fa-paint-roller"),
	PRINTER("fas fa-print"),
	SHEET_REDRAFT("fas fa-reply"),
	SHEET_DRAFT("fas fa-file-alt"),
	SHEET_ISSUED("far fa-file"),
	SHEET_ARCHIVE("fas fa-archive"),
	PHONE("fas fa-phone"),
	SEARCH("fas fa-search"),
	OFFICE("fas fa-building"),
	INFO("fas fa-info-circle"),
	MAIL("fas fa-envelope-open-text"),
	MORE("fas fa-ellipsis-h"),

	BTN_YES("fas fa-check"),
	BTN_NO("fas fa-times"),
	BTN_CANCEL("fas fa-running"),
	BTN_OK("fas fa-thumbs-up"),

	BTN_SIGN_IN("fas fa-sign-in-alt"),
	BTN_SIGN_OUT("fas fa-sign-out-alt"),
	BTN_SIGN_UP("fas fa-id-card"),

	BTN_EDIT("fas fa-pencil-alt"),
	BTN_ADD("fas fa-plus"),
	BTN_DEL("fas fa-minus"),
	BTN_PUT("fas fa-download"),
	BTN_GET("fas fa-upload"),
	BTN_REFRESH("fas fa-sync-alt"),

	BTN_REM_FROM_DRAFT("fas fa-file-export"),
	BTN_ADD_TO_DRAFT("fas fa-file-import"),

	USERROLE_ADMIN("fas fa-user-shield"),
	USERROLE_DISPATCHER("fas fa-user-edit"),
	USERROLE_MANAGER("fas fa-people-arrows"),
	USERROLE_PRODUCTOPER("fas fa-tools"),
	USERROLE_COURIER("fas fa-shipping-fast"),
	USERROLE_CLIENT("fas fa-male"),
	USERROLE_USER("fas fa-user-times"),

	COURIERVISITSTATE_NEW("fas fa-phone-volume"),
	COURIERVISITSTATE_RUNNING("fas fa-stopwatch"),
	COURIERVISITSTATE_COMPLETED("far fa-check-square"),
	COURIERVISITSTATE_CANCELLED("fas fa-trash-alt");

	/** The icon. */
	private final String classCSS;

	/**
	 * Instantiates a new UI icon.
	 *
	 * @param icon the icon
	 */

	private UIIcon(String classCSS) {
		this.classCSS = classCSS;
	}

	public String getClassCSS() {
		return classCSS;
	}

}