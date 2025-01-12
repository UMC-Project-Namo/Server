package com.namo.spring.application.external.api.user.converter;

import com.namo.spring.application.external.api.user.dto.MemberProfileResponse;
import com.namo.spring.db.mysql.domains.user.model.query.MemberWithFriendshipStatusQuery;
import com.namo.spring.db.mysql.domains.user.type.FriendshipStatus;

import java.time.format.DateTimeFormatter;

public class MemberProfileConverter {
    private static final String HIDDEN_VALUE = "비공개";

    public static MemberProfileResponse.MemberProfileDto toMemberProfileDto(MemberWithFriendshipStatusQuery dto) {
        String name = (dto.friendshipStatus()!=null && dto.friendshipStatus().equals(FriendshipStatus.ACCEPTED)) ? dto.member().getName() : HIDDEN_VALUE;
        String birthday = dto.member().isBirthdayVisible() ? dto.member().getBirthday().format(DateTimeFormatter.ofPattern("MM-dd")) : HIDDEN_VALUE;
        return MemberProfileResponse.MemberProfileDto.builder()
                .memberId(dto.member().getId())
                .profileImage(dto.member().getProfileImage())
                .nickname(dto.member().getNickname())
                .name(name)
                .tag(dto.member().getTag())
                .bio(dto.member().getBio())
                .birthday(birthday)
                .favoriteColorId(dto.member().getPalette().getId())
                .friendshipStatus(dto.friendshipStatus())
                .build();
    }
}


