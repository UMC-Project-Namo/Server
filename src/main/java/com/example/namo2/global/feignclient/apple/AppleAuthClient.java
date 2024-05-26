package com.example.namo2.global.feignclient.apple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppleAuthClient {
	private final Logger logger = LoggerFactory.getLogger(AppleAuthClient.class);
	private final AppleAuthApi appleAuthApi;
	private final AppleProperties appleProperties;

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

}
