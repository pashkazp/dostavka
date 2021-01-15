/*
 * 
 */
package ua.com.sipsoft.service.security.jwt;

import java.io.Serializable;
import java.util.Date;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.service.security.UserPrincipal;
import ua.com.sipsoft.util.AppProperties;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider implements Serializable {

	private static final long serialVersionUID = -2564335944320546370L;

	private final AppProperties appProperties;

	public String createToken(Authentication authentication) {
		log.debug("Get Principal Authentication '{}' and create JWT token", authentication);
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

		Claims claims = Jwts.claims().setSubject(userPrincipal.getEmail());
		claims.put("scopes", userPrincipal.getAuthorities());

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());
		return Jwts.builder()
				.setClaims(claims)
				.setIssuer(appProperties.getAuth().getTokenIssurer())
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
				.compact();
	}

	/**
	 * Checks if is token expired. Returns the true value if the expiration date
	 * taken from the token exceeds the current date or if more milliseconds have
	 * elapsed since the token was created than specified in the AppProperties
	 *
	 * @param token the token String
	 * @return the boolean
	 */
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date())
				|| (new Date().getTime() - getIssuedDateFromToken(token).getTime()) > appProperties.getAuth()
						.getTokenExpirationMsec();
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public Date getIssuedDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(appProperties.getAuth().getTokenSecret())
				.parseClaimsJws(token)
				.getBody();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername())
				&& !isTokenExpired(token));
	}

	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret())
					.parseClaimsJws(token);

			if (claims.getBody().getExpiration().before(new Date())) {
				return false;
			}

			return true;
		} catch (JwtException | IllegalArgumentException e) {
			throw new JwtAuthenticationException("JWT token is expired or invalid");
		}
	}

	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith(appProperties.getAuth().getTokenPrefix())) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

}
