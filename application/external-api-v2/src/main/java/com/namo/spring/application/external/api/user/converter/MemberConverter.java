package com.namo.spring.application.external.api.user.converter;

import com.namo.spring.application.external.api.user.dto.MemberResponse;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.type.SocialType;

import java.util.Map;

public class MemberConverter {

    private MemberConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static Member toMember(Map<String, String> response, String socialRefreshToken, SocialType socialType) {
        return Member.builder()
                .email(response.get("email"))
                .name(response.get("nickname"))
                .birthday(response.getOrDefault("birthday", null))
                .socialType(socialType)
                .socialRefreshToken(socialRefreshToken)
                .build();
    }

    public static Member toMember(String authId, String socialRefreshToken, SocialType socialType) {
        return Member.builder()
                .email(authId)
                .socialType(socialType)
                .socialRefreshToken(socialRefreshToken)
                .build();
    }

    public static MemberResponse.SignUpDoneDto toSignUpDoneDto(Member member) {
        return MemberResponse.SignUpDoneDto.builder()
                .nickname(member.getNickname())
                .tag(member.getTag())
                .name(member.getName())
                .bio(member.getBio())
                .birth(member.getBirthday())
                .colorId(member.getPalette().getId())
                .build();
    }

}
