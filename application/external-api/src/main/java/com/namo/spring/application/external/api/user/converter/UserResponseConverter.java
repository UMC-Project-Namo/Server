package com.namo.spring.application.external.api.user.converter;

import java.util.List;

import com.namo.spring.application.external.api.user.dto.UserResponse;

public class UserResponseConverter {
	private UserResponseConverter() {
		throw new IllegalStateException("Utility class");
	}

	public static UserResponse.SignUpDto toSignUpDto(
		String accessToken,
		String refreshToken,
		boolean isNewUser,
		List<UserResponse.TermsDto> terms
	) {
		return UserResponse.SignUpDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.newUser(isNewUser)
			.terms(terms)
			.build();
	}

	public static UserResponse.ReissueDto toReissueDto(String accessToken, String refreshToken) {
		return UserResponse.ReissueDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
