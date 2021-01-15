package ua.com.sipsoft.service.security;

import javax.servlet.ServletContext;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.common.VerificationToken;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.user.UserService;
import ua.com.sipsoft.service.util.AppNotificator;
import ua.com.sipsoft.service.util.VerificationTokenGenerator;
import ua.com.sipsoft.util.AppURL;
import ua.com.sipsoft.util.I18NProvider;
import ua.com.sipsoft.util.message.LoginMsg;
import ua.com.sipsoft.util.security.VerificationTokenType;

/**
 * The listener interface for receiving registration events. The class that is
 * interested in processing a registration event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addRegistrationListener<code> method. When the registration
 * event occurs, that object's appropriate method is invoked.
 *
 * @see RegistrationEvent
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RememberPasswordListener implements
		ApplicationListener<OnRememberPasswordEvent>, VerificationTokenGenerator {

	/** The User userService. */
	private final UserService userService;

	/** The mail sender. */
	private final JavaMailSender mailSender;

	/** The I18N Provider */
	private final I18NProvider i18n;

	private final ServletContext servletContext;

	/**
	 * On application event.
	 *
	 * @param event the event
	 */
	@Override
	public void onApplicationEvent(OnRememberPasswordEvent event) {
		this.confirmRegistration(event);
	}

	/** The from string. */
	@Value("${spring.mail.username}")
	private String fromString;

	/**
	 * Confirm registration.
	 *
	 * @param event the event
	 */
	private void confirmRegistration(OnRememberPasswordEvent event) {
		log.info("Create Remember Password Token and Send confirmation email.");
		SimpleMailMessage email = new SimpleMailMessage();
		User user = event.getUser();
		String token = makeVerificationToken();

		VerificationToken vToken = userService.createVerificationToken(user, token, VerificationTokenType.FORGOTPASS);
		if (vToken == null) {
			log.warn("Verification Token was not created. Confirmation email was not sended");
			return;
		}

		String recipientAddress = user.getEmail();

		String subject = i18n.getTranslation(LoginMsg.REMEMBER_PASS_SUBJ, event.getLocale());

		String confirmationUrl = AppURL.APP_DOMAIN + servletContext.getContextPath() + AppURL.LOGIN_FORGOT
				+ AppURL.REQUEST
				+ "?token=" + token;

		String message = i18n.getTranslation(LoginMsg.REMEMBER_PASS_MSG_BODY, event.getLocale());

		message = String.format(message, user.getName(), confirmationUrl);

		message = StringEscapeUtils.unescapeJava(message);

		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(message);
		email.setFrom(fromString);
		// TODO open me
		// mailSender.send(email);

		AppNotificator.notify(i18n.getTranslation(LoginMsg.REMEMBER_PASS_MSG_SENDED, event.getLocale()));
	}
}