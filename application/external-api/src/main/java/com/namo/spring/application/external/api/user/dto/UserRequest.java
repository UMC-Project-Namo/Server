package com.namo.spring.application.external.api.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

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
	@Schema(description = "소셜 회원가입 요청 DTO")
	public static class SocialSignUpDto {
		@Schema(description = "소셜 로그인을 통해 발급받은 accessToken", example = "exampleAccessToken")
		@NotBlank(message = "accessToken은 필수값입니다.")
		private String accessToken;
		@Schema(description = "소셜 로그인을 통해 발급받은 refreshToken", example = "exampleRefreshToken")
		@NotBlank(message = "refreshToken은 필수값입니다.")
		private String socialRefreshToken;
	}

	@Getter
	public static class AppleSignUpDto {
		@NotBlank
		private String authorizationCode;
		@NotBlank
		private String identityToken;
		private String email;
		private String username;
	}

	@NoArgsConstructor
	public static class TermDto {
		@NotNull
		private boolean isCheckTermOfUse;
		@NotNull
		private boolean isCheckPersonalInformationCollection;

		public boolean getIsCheckTermOfUse() {
			return isCheckTermOfUse;
		}

		public boolean getIsCheckPersonalInformationCollection() {
			return isCheckPersonalInformationCollection;
		}
	}

}
