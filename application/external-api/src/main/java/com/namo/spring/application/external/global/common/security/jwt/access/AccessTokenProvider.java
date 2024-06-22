package com.namo.spring.application.external.global.common.security.jwt.access;

import static com.namo.spring.application.external.global.common.security.jwt.access.AccessTokenClaimKeys.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.namo.spring.application.external.global.common.annotation.AccessTokenStrategy;
import com.namo.spring.core.common.utils.DateUtil;
import com.namo.spring.core.infra.common.jwt.JwtClaims;
import com.namo.spring.core.infra.common.jwt.JwtProvider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Primary
@Component
@AccessTokenStrategy
public class AccessTokenProvider implements JwtProvider {
	private final SecretKey secretKey;
	private final Duration tokenExpiration;

	public AccessTokenProvider(
		@Value("${jwt.secret-key.access-token}") String jwtSecretKey,
		@Value("${jwt.expiration-time.access-token}") Duration tokenExpiration
	) {
		byte[] keyBytes = Base64.getDecoder().decode(jwtSecretKey);
		this.secretKey = Keys.hmacShaKeyFor(keyBytes);
		this.tokenExpiration = tokenExpiration;
	}

	@Override
	public String createToken(JwtClaims claims) {
		Date now = new Date();

		return Jwts.builder()
			.setHeader(createHeader())
			.setClaims(claims.getClaims())
			.signWith(secretKey)
			.setExpiration(createExpirationDate(now, tokenExpiration.toMillis()))
			.compact();
	}

	@Override
	public JwtClaims parseJwtClaimsFromToken(String token) {
		Claims claims = getClaimsFromToken(token);
		return AccessTokenClaim.of(
			Long.parseLong(claims.get(USER_ID.getValue(), String.class))
		);
	}

	@Override
	public LocalDateTime getExpiredDate(String token) {
		Claims claims = getClaimsFromToken(token);
		return DateUtil.toLocalDateTime(claims.getExpiration());
	}

	// TODO: Custom exception 적용
	@Override
	public boolean isTokenExpired(String token) {
		try {
			Claims claims = getClaimsFromToken(token);
			return claims.getExpiration().before(new Date());
		} catch (Exception e) {
			log.error("Token is expired: {}", e.getMessage());
			throw e;
		}
	}

	// TODO: Custom exception 적용
	@Override
	public Claims getClaimsFromToken(String token) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (Exception e) {
			log.warn("Token is invalid: {}", e.getMessage());
			throw e;
		}
	}

	private Map<String, Object> createHeader() {
		return Map.of(
			"typ", "JWT",
			"alg", "HS256",
			"regDate", System.currentTimeMillis()
		);
	}

	private Date createExpirationDate(Date now, long expirationTime) {
		return new Date(now.getTime() + expirationTime);
	}
}
