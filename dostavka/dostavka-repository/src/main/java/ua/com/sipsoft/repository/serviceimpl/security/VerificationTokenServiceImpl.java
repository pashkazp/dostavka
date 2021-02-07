package ua.com.sipsoft.repository.serviceimpl.security;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.dao.common.VerificationToken;
import ua.com.sipsoft.repository.serviceimpl.mapper.security.VerificationTokenMapper;
import ua.com.sipsoft.repository.user.VerificationTokenRepository;
import ua.com.sipsoft.service.dto.VerificationTokenDto;
import ua.com.sipsoft.service.security.VerificationTokenService;

/**
 * The class represent class that store and retrieve {@link VerificationToken}.
 *
 * @author Pavlo Degtyaryev
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

	/** The dao. */
	private final VerificationTokenRepository dao;
	private final VerificationTokenMapper verificationTokenMapper;

//	private final VerificationTokenMapper verificationTokenMapper;

	/**
	 * Save verification token.
	 *
	 * @param token the token
	 * @return the verification token
	 */
	@Override
	public VerificationToken saveVerificationToken(VerificationToken token) {
		log.info("Save verification token \"{}\"", token);
		if (token == null) {
			log.warn("Store verification token impossible. Missing some data. ");
			return null;
		}
		try {
			return dao.save(token);
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
		return null;
	}

	/**
	 * Fetch Optional VerificationToken by token string.
	 *
	 * @param token the token
	 * @return the optional
	 */
	@Override
	public Optional<VerificationToken> fetchByToken(String token) {
		log.debug("Get Verification token by token: '{}'", token);
		if (token == null) {
			log.debug("Gets Verification token by token is impossible. Token is null.");
			return Optional.empty();
		}
		try {
			return dao.findByToken(token);
		} catch (Exception e) {
			log.error("The Verification token by token is not received for a reason: {}", e.getMessage());
			return Optional.empty();
		}
	}

	/**
	 * Delete Token by token string.
	 *
	 * @param token the token
	 */
	@Override
	public void deleteByTokenString(String token) {
		log.debug("Delete Verification token by token: '{}'", token);
		if (token == null) {
			log.debug("Delete Verification token by token is impossible. Token is null.");
		}
		try {
			dao.deleteByToken(token);
		} catch (Exception e) {
			log.error(" Verification token by token is not deleted for a reason: {}", e.getMessage());
		}
	}

	/**
	 * Fetch Optional VerificationTokenDto by token string.
	 *
	 * @param token the token
	 * @return the optional
	 */
	@Override
	public Optional<VerificationTokenDto> fetchDtoByToken(String token) {
		log.debug("Get Verification DTO token by token: '{}'", token);
		if (token == null) {
			log.debug("Gets Verification DTO token by token is impossible. Token is null.");
			return Optional.empty();
		}
		try {
			Optional<VerificationToken> vToken = fetchByToken(token);
			if (vToken.isEmpty()) {
				return Optional.empty();
			} else {
				VerificationTokenDto verificationTokenDto = verificationTokenMapper.toDto(vToken.get());
				return Optional.of(verificationTokenDto);
			}
		} catch (Exception e) {
			log.error("The Verification DTO token by token is not received for a reason: {}", e.getMessage());
			return Optional.empty();
		}
	}

	/**
	 * Sets the token used by token string.
	 *
	 * @param token the new token used
	 */
	@Override
	public void setTokenUsed(String token) {
		log.info("Set the token  is used \"{}\"", token);
		if (token == null) {
			log.warn("Set the token used is impossible. Missing some data. ");
			return;
		}

		Optional<VerificationToken> vTokenOpt = fetchByToken(token);
		if (!vTokenOpt.isEmpty()) {
			VerificationToken vToken = vTokenOpt.get();
			vToken.setUsedDate(LocalDateTime.now());
			vToken.setUsed(true);
			saveVerificationToken(vToken);
		}

	}

}