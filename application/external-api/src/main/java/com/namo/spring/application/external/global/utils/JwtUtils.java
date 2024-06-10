package com.example.namo2.global.utils;

import static com.example.namo2.global.common.response.BaseResponseStatus.*;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@Component
public class JwtUtils {
	private static final String HEADER = "Authorization";
	private static final Long ACCESS_TOKEN_EXPIRED_TIME = 1000 * 60 * 60 * 12L;
	private static final Long REFRESH_TOKEN_EXPIRED_TIME = 1000 * 60 * 60 * 24 * 14 * 1L;

	@Value("${jwt.secret-key}")
	private String secretKey;

	public String[] generateTokens(Long userId) {
		String accessToken = createAccessToken(userId);
		String refreshToken = createRefreshToken(userId);
		return new String[] {accessToken, refreshToken};
	}

	private String createAccessToken(Long userId) {
		return createJwt(userId, 1000 * 1000 * 1000 * ACCESS_TOKEN_EXPIRED_TIME);
	}

	private String createRefreshToken(Long userId) {
		return createJwt(userId, REFRESH_TOKEN_EXPIRED_TIME);
	}

	private String createJwt(Long userId, Long tokenValid) {
		byte[] keyBytes = Decoders.BASE64.decode(getSecretKey());
		Key key = Keys.hmacShaKeyFor(keyBytes);

		return Jwts.builder()
			.signWith(key)
			.claim("userId", userId)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + tokenValid))
			.compact();
	}

	public Long resolveRequest(HttpServletRequest request) throws BaseException {
		try {
			String accessToken = getAccessToken(request);
			return resolveToken(accessToken);
		} catch (ExpiredJwtException e) {
			throw new BaseException(EXPIRATION_ACCESS_TOKEN);
		}
	}

	public String getAccessToken(HttpServletRequest request) throws BaseException {
		String accessToken = request.getHeader(HEADER);

		if (accessToken == null || accessToken.length() == 0) {
			throw new BaseException(BaseResponseStatus.EMPTY_ACCESS_KEY);
		}

		return getJwtToken(accessToken);
	}

	private static String getJwtToken(String accessToken) {
		return accessToken.replace("Bearer ", "");
	}

	private Long resolveToken(String accessToken) throws BaseException {
		return Optional.ofNullable(Jwts.parserBuilder()
				.setSigningKey(getSecretKey())
				.build()
				.parseClaimsJws(accessToken)
				.getBody())
			.map((c) -> c.get("userId", Long.class))
			.orElseThrow(() -> new BaseException(BaseResponseStatus.EMPTY_ACCESS_KEY));
	}

	public boolean validateRequest(HttpServletRequest request) {
		try {
			String jwtToken = getAccessToken(request);
			return validateToken(jwtToken);
		} catch (Exception e) {
			return false;
		}
	}

	public boolean validateToken(String token) {
		Jws<Claims> claims = Jwts.parserBuilder()
			.setSigningKey(getSecretKey())
			.build()
			.parseClaimsJws(getJwtToken(token));
		return !claims.getBody().getExpiration().before(new Date());
	}

	private String getSecretKey() {
		String secretKeyEncodeBase64 = Encoders.BASE64.encode(secretKey.getBytes());
		return secretKeyEncodeBase64;
	}

	public Long getExpiration(String accessToken) {
		try {
			Jws<Claims> claims = Jwts.parserBuilder()
				.setSigningKey(getSecretKey())
				.build()
				.parseClaimsJws(accessToken);
			return claims.getBody().getExpiration().getTime();
		} catch (Exception e) {
			throw new BaseException(EXPIRATION_ACCESS_TOKEN);
		}
	}
}
