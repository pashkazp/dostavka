package ua.com.sipsoft.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.sipsoft.dao.common.VerificationToken;

/**
 * The Interface VerificationTokenRepository.
 */
public interface VerificationTokenRepository
		extends JpaRepository<VerificationToken, Long> {

	/**
	 * Find {@link VerificationToken} by token string.
	 *
	 * @param token the token
	 * @return the optional
	 */
	Optional<VerificationToken> findByToken(String token);

	void deleteByToken(String token);
}