package com.namo.spring.client.social.apple.client;

import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.namo.spring.client.social.apple.api.AppleAuthApi;
import com.namo.spring.client.social.apple.dto.AppleResponse;
import com.namo.spring.client.social.apple.dto.AppleResponseConverter;
import com.namo.spring.client.social.common.properties.AppleProperties;
import com.namo.spring.client.social.common.utils.AppleUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppleAuthClient {

	private static final String EXPECTED_ISSUER = "https://appleid.apple.com";
	private static final String GRANT_TYPE_AUTH_CODE = "authorization_code";
	private static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
	private static final String TOKEN_TYPE_ACCESS = "access_token";
	private static final String CLAIM_KID = "kid";
	private static final String CLAIM_ALG = "alg";
	private static final String CLAIM_ISSUER = "iss";
	private static final String CLAIM_AUDIENCE = "aud";

	private final Logger logger = LoggerFactory.getLogger(AppleAuthClient.class);
	private final AppleAuthApi appleAuthApi;
	private final AppleProperties appleProperties;
	private final AppleUtils appleUtils;

	public AppleResponse.ApplePublicKeyListDto getApplePublicKeys() {
		return appleAuthApi.getApplePublicKeys();
	}

	public String getAppleRefreshToken(String clientSecret, String autorizationCode) {
		logger.debug("{}", appleProperties.getClientId());
		AppleResponse.GetToken getToken = appleAuthApi.getAppleRefreshToken(
			appleProperties.getClientId(),
			clientSecret,
			autorizationCode,
			GRANT_TYPE_AUTH_CODE,
			appleProperties.getRedirectUri()
		);

		logger.debug("getAppleToken : {}", getToken.getAccessToken());
		return getToken.getRefreshToken();
	}

	public String getAppleAccessToken(String clientSecret, String refreshToken) {
		logger.debug("{}", appleProperties.getClientId());
		AppleResponse.GetToken getToken = appleAuthApi.getAppleAccessToken(
			appleProperties.getClientId(),
			clientSecret,
			refreshToken,
			GRANT_TYPE_REFRESH_TOKEN
		);

		logger.debug("getAppleToken : {}", getToken.getAccessToken());
		return getToken.getAccessToken();
	}

	public AppleResponse.ApplePublicKeyDto getApplePublicKey(AppleResponse.ApplePublicKeyListDto applePublicKeys,
		JSONObject headerJson) {
		Object kid = headerJson.get(CLAIM_KID);
		Object alg = headerJson.get(CLAIM_ALG);
		return AppleResponseConverter.toApplePublicKey(applePublicKeys, alg, kid);
	}

	public void revoke(String clientSecret, String token) {
		logger.debug("[client] token {}", token);
		appleAuthApi.revoke(
			appleProperties.getClientId(),
			clientSecret,
			token,
			TOKEN_TYPE_ACCESS
		);
	}

	public String createClientSecret() {
		Date expirationDate = Date.from(
			LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());

		return Jwts.builder()
			.setHeaderParam(CLAIM_KID, appleProperties.getKeyId())
			.setHeaderParam(CLAIM_ALG, "ES256")
			.setIssuer(appleProperties.getTeamId())
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(expirationDate)
			.setAudience(EXPECTED_ISSUER)
			.setSubject(appleProperties.getClientId())
			.signWith(SignatureAlgorithm.ES256, appleUtils.getPrivateKey())
			.compact();
	}

	/**
	 * Apple로부터 받은 토큰을 검증합니다.
	 *
	 * @param publicKey : Apple로부터 받은 공개키
	 * @param token     : Apple로부터 받은 토큰
	 */
	public void validateToken(PublicKey publicKey, String token) {
		Claims claims = parseToken(publicKey, token);
		validateIssuer(claims);
		validateAudience(claims);
		validateExpiration(claims);
	}

	private Claims parseToken(PublicKey publicKey, String token) {
		return Jwts.parser().setSigningKey(publicKey).build().parseClaimsJws(token).getBody();
	}

	private void validateIssuer(Claims claims) {
		String issuer = getClaimAsString(claims, CLAIM_ISSUER);
		if (!EXPECTED_ISSUER.equals(issuer)) {
			throw new IllegalArgumentException("Invalid issuer");
		}
	}

	private void validateAudience(Claims claims) {
		Object audienceObj = claims.get(CLAIM_AUDIENCE);
		log.debug("apple aud {}", audienceObj);

		if (audienceObj instanceof String) {
			validateSingleAudience((String)audienceObj);
		} else if (audienceObj instanceof LinkedHashSet<?>) {
			validateMultipleAudiences((LinkedHashSet<?>)audienceObj);
		} else {
			throw new IllegalArgumentException("Audience is not a string or set");
		}
	}

	private void validateSingleAudience(String audience) {
		log.debug("Single audience: {}", audience);
		if (!appleProperties.getClientId().equals(audience)) {
			throw new IllegalArgumentException("Invalid audience");
		}
	}

	private void validateMultipleAudiences(LinkedHashSet<?> audienceSet) {
		log.debug("Multiple audiences: {}, size: {}", audienceSet, audienceSet.size());
		boolean isValid = audienceSet.stream()
			.filter(String.class::isInstance)
			.map(String.class::cast)
			.anyMatch(aud -> appleProperties.getClientId().equals(aud));

		if (!isValid) {
			throw new IllegalArgumentException("Invalid audience");
		}
	}

	private void validateExpiration(Claims claims) {
		Date expirationDate = claims.getExpiration();
		Date now = new Date();
		log.debug("expiration: {} < now: {}", expirationDate.getTime(), now.getTime());
		if (expirationDate.before(now)) {
			throw new IllegalArgumentException("Token expired");
		}
	}

	private String getClaimAsString(Claims claims, String claimName) {
		return Optional.ofNullable(claims.get(claimName))
			.filter(String.class::isInstance)
			.map(String.class::cast)
			.orElseThrow(() -> new IllegalArgumentException(claimName + " is not a string"));
	}

}
