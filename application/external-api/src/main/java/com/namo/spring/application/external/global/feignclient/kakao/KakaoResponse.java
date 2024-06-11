package com.namo.spring.application.external.global.feignclient.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

public class KakaoResponse {
	public KakaoResponse() {
		throw new RuntimeException("utility class");
	}

	@Getter
	public static class GetAccessToken {
		@JsonProperty("token_type")
		private String tokenType;
		@JsonProperty("access_token")
		private String accessToken;
		@JsonProperty("id_token")
		private String idToken;
		@JsonProperty("expires_in")
		private Integer expiresIn;
		@JsonProperty("refresh_token")
		private String refreshToken;
		@JsonProperty("refresh_token_expires_in")
		private Integer refreshTokenExpiresIn;
	}

	@Getter
	public static class UnlinkDto {
		private Long id;
	}
}
