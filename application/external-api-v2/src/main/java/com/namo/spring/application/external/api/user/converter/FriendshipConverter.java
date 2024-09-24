package com.namo.spring.application.external.api.user.converter;

import com.namo.spring.application.external.api.user.dto.FriendshipResponse;
import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.entity.Member;

public class FriendshipConverter {

    private static final String BIRTHDAY_HIDDEN = "비공개";

    public static Friendship toFriendShip(Member request, Member target){
        return Friendship.builder()
                .member(request)
                .friend(target)
                .build();
    }

    public static FriendshipResponse.FriendRequestDto toFriendRequestDto(Friendship friendship){
        Member friend = friendship.getFriend();
        String birthday = friend.isBirthdayVisible() ? friend.getBirthday() : BIRTHDAY_HIDDEN;
        return FriendshipResponse.FriendRequestDto.builder()
                .friendRequestId(friendship.getId())
                .memberId(friend.getId())
                .profileImage(friend.getProfileImage())
                .nickname(friend.getNickname())
                .tag(friend.getTag())
                .bio(friend.getBio())
                .birthday(birthday)
                .favoriteColorId(friend.getPalette().getId())
                .build();
    }
}
