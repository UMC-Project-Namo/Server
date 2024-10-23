package com.namo.spring.application.external.api.user.converter;

import com.namo.spring.application.external.api.user.dto.FriendshipResponse;
import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.Page;

public class FriendshipConverter {

    private static final String BIRTHDAY_HIDDEN = "비공개";

    public static Friendship toFriendShip(Member request, Member target){
        return Friendship.builder()
                .member(request)
                .friend(target)
                .build();
    }

    public static FriendshipResponse.GetFriendRequestDto toGetFriendRequestDto(Page<Friendship> friendships){
        List<FriendshipResponse.FriendRequestDto> friendRequestDto = friendships.stream()
                .map(FriendshipConverter::toFriendRequestDto)
                .toList();
        return FriendshipResponse.GetFriendRequestDto.builder()
                .friendRequests(friendRequestDto)
                .currentPage(friendships.getNumber()+1)
                .pageSize(friendships.getSize())
                .totalPages(friendships.getTotalPages())
                .totalItems(friendships.getTotalElements())
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
