package com.namo.spring.application.external.api.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberRequest {
	private MemberRequest() {
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
		@NotBlank
		private String socialRefreshToken;
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
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
