package ua.com.sipsoft.util;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Class I18NProvider returns translated message by message key and Locale.
 * 
 * @author Pavlo Degtyaryev
 * @version 1.0
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class I18NProvider {

	/** The message source. */
	private final MessageSource messageSource;

	/**
	 * Gets the translation.
	 *
	 * @param message the message key
	 * @param locale  the {@link Locale}}
	 * @return the translation {@link String} of message
	 */
	public String getTranslation(String message, Locale locale) {

		log.info("I18NProvider call: \"{}\"", message);
		return messageSource.getMessage(message, null, locale);
	}

}
