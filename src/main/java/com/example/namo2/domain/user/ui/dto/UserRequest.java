package com.example.namo2.domain.user.ui.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequest {
	private UserRequest() {
		throw new IllegalStateException("Utility class");
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class LogoutDto {
		@NotBlank
		private String accessToken;
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class SignUpDto {
		@NotBlank
		private String accessToken;
		@NotBlank
		private String refreshToken;
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class SocialSignUpDto {
		@NotBlank
		private String accessToken;
	}

	@Getter
	public static class AppleSignUpDto{
		@NotBlank
		private String identityToken;
		private String email;
		private String username;
	}

}
