package ua.com.sipsoft.service.security;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ua.com.sipsoft.dao.common.VerificationToken;
import ua.com.sipsoft.service.dto.VerificationTokenDto;

/**
 * The interface VerificationTokenService represent class that store and
 * retrieve {@link VerificationToken}.
 *
 * @author Pavlo Degtyaryev
 */

@Service
public interface VerificationTokenService {

	/**
	 * Save verification token.
	 *
	 * @param token the token
	 * @return the verification token
	 */
	VerificationToken saveVerificationToken(VerificationToken token);

	/**
	 * Fetch VerificationToken by token string.
	 *
	 * @param token the token
	 * @return the optional
	 */
	public Optional<VerificationToken> fetchByToken(String token);

	/**
	 * Fetch VerificationTokenDto by token.
	 *
	 * @param token the token
	 * @return the optional
	 */
	public Optional<VerificationTokenDto> fetchDtoByToken(String token);

	/**
	 * Sets the token used.
	 *
	 * @param token the new token used
	 */
	public void setTokenUsed(String token);

	/**
	 * Delete by token by token string.
	 *
	 * @param token the token
	 */
	void deleteByTokenString(String token);

}