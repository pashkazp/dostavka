package ua.com.sipsoft.service.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.util.security.VerificationTokenType;

/**
 * A class that is a token issued to a client and designed to verify changes
 * requested by the user
 *
 * @author Pavlo Degtyaryev
 */
@EqualsAndHashCode(of = { "id" })
@ToString(exclude = { "token" })
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerificationTokenDto implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3605714338727098314L;

	/** The id. */
	private Long id;

	/** The token. */
	private String token;

	/** The token type. */
	private VerificationTokenType tokenType;

	/** The user. */
	private User user;

	/** The expiry date. */
	private LocalDateTime expiryDate = LocalDateTime.now();

	/** The used. */
	private Boolean used = false;

	/** The used date. */
	private LocalDateTime usedDate;

	/**
	 * Instantiates a new verification token dto.
	 *
	 * @param user      the user
	 * @param token     the token
	 * @param tokenType the token type
	 */
	public VerificationTokenDto(User user, String token, VerificationTokenType tokenType) {
		super();
		this.user = user;
		this.token = token;
		this.tokenType = tokenType;
		this.expiryDate = LocalDateTime.now().plusMinutes(60);
	}
}