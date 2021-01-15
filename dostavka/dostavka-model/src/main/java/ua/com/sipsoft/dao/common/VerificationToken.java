package ua.com.sipsoft.dao.common;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.dao.util.VerificationTokenConvertor;
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
@Table(name = "users_verification_tokens")
@Entity
public class VerificationToken implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3605714338727098314L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "token_id")
	private Long id;

	/** The token. */
	@Column(name = "token", nullable = false, length = 200, unique = true)
	private String token;

	/** The token type. */
	@Column
	@Convert(converter = VerificationTokenConvertor.class)
	private VerificationTokenType tokenType;

	/** The user. */
	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;

	/** The expiry date. */
	private LocalDateTime expiryDate = LocalDateTime.now();

	/** The enabled. */
	@Column(name = "enabled", nullable = false)
	private Boolean used = false;

	/** The used date. */
	private LocalDateTime usedDate;

	/**
	 * Instantiates a new verification token.
	 *
	 * @param user      the user
	 * @param token     the token
	 * @param tokenType the token type
	 */
	public VerificationToken(User user, String token, VerificationTokenType tokenType) {
		super();
		this.user = user;
		this.token = token;
		this.tokenType = tokenType;
		this.expiryDate = LocalDateTime.now().plusMinutes(60);
	}

}