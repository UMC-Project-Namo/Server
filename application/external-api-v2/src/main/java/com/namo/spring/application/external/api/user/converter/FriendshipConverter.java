package com.namo.spring.application.external.api.user.converter;

import com.namo.spring.application.external.api.user.dto.FriendshipResponse;
import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FriendshipConverter {

    private static final String BIRTHDAY_HIDDEN = "비공개";

    public static Friendship toFriendShip(Member request, Member target){
        return Friendship.builder()
                .member(request)
                .friend(target)
                .build();
    }

    public static FriendshipResponse.FriendRequestDto toFriendRequestDto(Friendship friendship){
        Member requestMember = friendship.getMember();
        String birthday = requestMember.isBirthdayVisible() ? requestMember.getBirthday().format(DateTimeFormatter.ofPattern("MM-dd")) : BIRTHDAY_HIDDEN;
        return FriendshipResponse.FriendRequestDto.builder()
                .friendRequestId(friendship.getId())
                .memberId(requestMember.getId())
                .profileImage(requestMember.getProfileImage())
                .nickname(requestMember.getNickname())
                .tag(requestMember.getTag())
                .bio(requestMember.getBio())
                .birthday(birthday)
                .favoriteColorId(requestMember.getPalette().getId())
                .build();
    }
}
