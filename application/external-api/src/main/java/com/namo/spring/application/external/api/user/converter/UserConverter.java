package com.namo.spring.application.external.api.user.converter;

import java.util.Map;

import com.namo.spring.db.mysql.domains.user.domain.User;
import com.namo.spring.db.mysql.domains.user.type.SocialType;
import com.namo.spring.db.mysql.domains.user.type.UserStatus;

public class UserConverter {

	private UserConverter() {
		throw new IllegalStateException("Utility class");
	}

	public static User toUser(Map<String, String> response, String socialRefreshToken, SocialType socialType) {
		return User.builder()
			.email(response.get("email"))
			.name(response.get("nickname"))
			.birthday(response.getOrDefault("birthday", null))
			.status(UserStatus.ACTIVE)
			.socialType(socialType)
			.socialRefreshToken(socialRefreshToken)
			.build();
	}

	public static User toUser(String email, String name, String socialRefreshToken, SocialType socialType) {
		return User.builder()
			.email(email)
			.name(name)
			.status(UserStatus.ACTIVE)
			.socialType(socialType)
			.socialRefreshToken(socialRefreshToken)
			.build();
	}

}
