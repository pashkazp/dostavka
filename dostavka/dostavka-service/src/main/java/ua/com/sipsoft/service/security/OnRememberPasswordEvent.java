package ua.com.sipsoft.service.security;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.user.User;

@Getter
@Setter
@Slf4j
public class OnRememberPasswordEvent extends ApplicationEvent {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8908814679946292298L;

	/** The locale. */
	private Locale locale;

	/** The user. */
	private User user;

	/**
	 * Instantiates a new on registration complete event.
	 *
	 * @param user   the user
	 * @param locale the locale
	 * @param appUrl the app url
	 */
	public OnRememberPasswordEvent(
			User user, Locale locale) {
		super(user);
		log.info("Create completition registration event for Locale \"{}\", new User \"{}\"",
				locale, user);
		this.user = user;
		this.locale = locale;
	}

}