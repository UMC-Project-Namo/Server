package com.namo.spring.application.external.api.user.converter;

import java.util.Map;

import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.type.MemberStatus;
import com.namo.spring.db.mysql.domains.user.type.SocialType;

public class MemberConverter {

	private MemberConverter() {
		throw new IllegalStateException("Utility class");
	}

	public static Member toMember(Map<String, String> response, String socialRefreshToken, SocialType socialType) {
		return Member.builder()
			.email(response.get("email"))
			.name(response.get("nickname"))
			.birthday(response.getOrDefault("birthday", null))
			.status(MemberStatus.ACTIVE)
			.socialType(socialType)
			.socialRefreshToken(socialRefreshToken)
			.build();
	}

	public static Member toMember(String email, String name, String socialRefreshToken, SocialType socialType) {
		return Member.builder()
			.email(email)
			.name(name)
			.status(MemberStatus.ACTIVE)
			.socialType(socialType)
			.socialRefreshToken(socialRefreshToken)
			.build();
	}

}
