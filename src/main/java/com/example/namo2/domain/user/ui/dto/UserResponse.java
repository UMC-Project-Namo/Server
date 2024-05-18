package com.example.namo2.domain.user.ui.dto;

import lombok.Builder;
import lombok.Getter;

public class UserResponse {

	private UserResponse() {
		throw new IllegalStateException("Utility class");
	}

	@Getter
	@Builder
	public static class SignUpDto {
		private String accessToken;
		private String refreshToken;
		private boolean newUser;
	}

	@Getter
	@Builder
	public static class ReissueDto {
		private String accessToken;
		private String refreshToken;
	}
}
