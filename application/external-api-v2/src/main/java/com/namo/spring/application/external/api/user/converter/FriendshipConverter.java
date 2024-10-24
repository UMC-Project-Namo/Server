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

    public static FriendshipResponse.FriendRequestListDto toFriendRequestDto(Page<Friendship> friendships){
        List<FriendshipResponse.FriendRequestDto> friendRequestDto = friendships.stream()
                .map(FriendshipConverter::toFriendRequestDto)
                .toList();
        return FriendshipResponse.FriendRequestListDto.builder()
                .friendRequests(friendRequestDto)
                .currentPage(friendships.getNumber()+1)
                .pageSize(friendships.getSize())
                .totalPages(friendships.getTotalPages())
                .totalItems(friendships.getTotalElements())
                .build();
    }

    private static FriendshipResponse.FriendRequestDto toFriendRequestDto(Friendship friendship){
        Member requestMember = friendship.getMember();
        // todo : birthday 로직 일원화 필요 2024.10.23 Castle
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

    public static FriendshipResponse.FriendListDto toFriendListDto(Page<Friendship> friendships) {
        List<FriendshipResponse.FriendInfoDto> friendList = friendships.stream()
                .map(FriendshipConverter::toFriendInfoDto)
                .toList();
        return FriendshipResponse.FriendListDto.builder()
                .friendList(friendList)
                .currentPage(friendships.getNumber()+1)
                .pageSize(friendships.getSize())
                .totalPages(friendships.getTotalPages())
                .totalItems(friendships.getTotalElements())
                .build();
    }

    private static FriendshipResponse.FriendInfoDto toFriendInfoDto(Friendship friendship){
        Member friend = friendship.getFriend();
        String birthday = friend.isBirthdayVisible() ? friend.getBirthday().format(DateTimeFormatter.ofPattern("MM-dd")) : BIRTHDAY_HIDDEN;
        return FriendshipResponse.FriendInfoDto.builder()
                .memberId(friend.getId())
                .favoriteFriend(friendship.isFavorite())
                .profileImage(friend.getProfileImage())
                .nickname(friend.getNickname())
                .name(friend.getName())
                .tag(friend.getTag())
                .bio(friend.getBio())
                .birthday(birthday)
                .favoriteColorId(friend.getPalette().getId())
                .build();
    }
}
