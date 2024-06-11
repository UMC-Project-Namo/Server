package com.namo.spring.application.external.global.feignclient.naver;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public class NaverResponse {
	public NaverResponse() {
		throw new IllegalStateException("Utility class.");
	}

	@Getter
	public static class GetAccessToken {
		@JsonProperty("access_token")
		private String accessToken;
	}

	@Getter
	@Setter
	public static class UnlinkDto {
		@JsonProperty("access_token")
		private String accessToken;
		private String result;
	}

	@Getter
	public static class Availability {
		private String resultcode;
		private String message;
	}
}

