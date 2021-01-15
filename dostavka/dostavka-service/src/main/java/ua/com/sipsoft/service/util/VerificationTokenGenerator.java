package ua.com.sipsoft.service.util;

import java.util.UUID;

import ua.com.sipsoft.dao.common.VerificationToken;

/**
 * The interface VerificationTokenGenerator represent class that store and
 * retrieve {@link VerificationToken}.
 *
 * @author Pavlo Degtyaryev
 */

public interface VerificationTokenGenerator {
	default String makeVerificationToken() {
		return UUID.randomUUID().toString();
	}
}
