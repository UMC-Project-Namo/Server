package com.namo.spring.client.social.apple.client;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.namo.spring.client.social.apple.api.AppleAuthApi;
import com.namo.spring.client.social.apple.dto.AppleResponse;
import com.namo.spring.client.social.common.properties.AppleProperties;
import com.namo.spring.client.social.common.utils.AppleUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppleAuthClient {
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
			"authorization_code",
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
			"refresh_token"
		);

		logger.debug("getAppleToken : {}", getToken.getAccessToken());
		return getToken.getAccessToken();
	}

	public void revoke(String clientSecret, String token) {
		logger.debug("[client] token {}", token);
		appleAuthApi.revoke(
			appleProperties.getClientId(),
			clientSecret,
			token,
			"access_token"
		);
	}

	public String createClientSecret() {
		Date expirationDate = Date.from(
			LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());

		return Jwts.builder()
			.setHeaderParam("kid", appleProperties.getKeyId())
			.setHeaderParam("alg", "ES256")
			.setIssuer(appleProperties.getTeamId())
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(expirationDate)
			.setAudience("https://appleid.apple.com")
			.setSubject(appleProperties.getClientId())
			.signWith(SignatureAlgorithm.ES256, appleUtils.getPrivateKey())
			.compact();
	}

}
