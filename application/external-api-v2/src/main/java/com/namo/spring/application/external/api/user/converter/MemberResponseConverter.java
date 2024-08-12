package com.namo.spring.application.external.api.user.converter;

import java.util.List;

import com.namo.spring.application.external.api.user.dto.MemberResponse;

public class MemberResponseConverter {
	private MemberResponseConverter() {
		throw new IllegalStateException("Utility class");
	}

	public static MemberResponse.SignUpDto toSignUpDto(
		String accessToken,
		String refreshToken,
		boolean isNewUser,
		boolean isSignUpComplete,
		List<MemberResponse.TermsDto> terms
	) {
		return MemberResponse.SignUpDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.terms(terms)
			.newUser(isNewUser)
			.signUpComplete(isSignUpComplete)
			.build();
	}

	public static MemberResponse.ReissueDto toReissueDto(String accessToken, String refreshToken) {
		return MemberResponse.ReissueDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
