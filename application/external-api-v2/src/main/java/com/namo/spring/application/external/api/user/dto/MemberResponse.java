package com.namo.spring.application.external.api.user.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

public class MemberResponse {

	private MemberResponse() {
		throw new IllegalStateException("Utility class");
	}

	@Getter
	@Builder
	public static class SignUpDto {
		private String accessToken;
		private String refreshToken;
		private boolean newUser;
		private boolean signUpComplete;
		private List<TermsDto> terms;
	}

	@Getter
	@Builder
	public static class TermsDto {
		private String content;
		private boolean isCheck;
	}

	@Getter
	@Builder
	public static class ReissueDto {
		private String accessToken;
		private String refreshToken;
	}
}
