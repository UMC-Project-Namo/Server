package com.namo.spring.application.external.api.user.usecase;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.user.converter.FriendCategoryConverter;
import com.namo.spring.application.external.api.user.converter.FriendshipConverter;
import com.namo.spring.application.external.api.user.dto.FriendCategoryResponse;
import com.namo.spring.application.external.api.user.dto.FriendshipResponse;
import com.namo.spring.application.external.api.user.service.FriendManageService;
import com.namo.spring.application.external.api.user.service.MemberManageService;
import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FriendUseCase {

    private final MemberManageService memberManageService;
    private final FriendManageService friendManageService;

    @Transactional
    public void requestFriendship(Long myMemberId, String nicknameTag) {
        Member me = memberManageService.getActiveMember(myMemberId);
        Member friend = memberManageService.getActiveMemberByNicknameTag(nicknameTag);
        friendManageService.requestFriendShip(me, friend);
    }

    @Transactional(readOnly = true)
    public FriendshipResponse.FriendRequestListDto getFriendshipRequest(Long memberId, int page) {
        Page<Friendship> receivedRequests = friendManageService.getReceivedFriendRequests(memberId, page);
        return FriendshipConverter.toFriendRequestDto(receivedRequests);
    }

    @Transactional
    public void acceptRequest(Long memberId, Long friendshipId) {
        Friendship friendship = friendManageService.getPendingFriendship(friendshipId);
        friendManageService.acceptRequest(memberId, friendship);
    }

    @Transactional
    public void rejectFriendRequest(Long memberId, Long friendshipId) {
        Friendship friendship = friendManageService.getPendingFriendship(friendshipId);
        friendManageService.rejectRequest(memberId, friendship);
    }

    @Transactional(readOnly = true)
    public FriendshipResponse.FriendListDto getFriendList(Long userId, int page, String search) {
        Page<Friendship> friendships = friendManageService.getAcceptedFriendship(userId, page, search);
        return FriendshipConverter.toFriendListDto(friendships);
    }

    @Transactional
    public boolean toggleFavorite(Long memberId, Long friendId) {
        Friendship friendship = friendManageService.getAcceptedFriendship(memberId, friendId);
        friendship.toggleFavorite();
        return friendship.isFavorite();
    }

    @Transactional
    public void deleteFriend(Long memberId, Long friendId) {
        Friendship target = friendManageService.getAcceptedFriendship(memberId, friendId);
        Friendship reversedTarget = friendManageService.getAcceptedFriendship(friendId, memberId);
        friendManageService.deleteFriendShip(target, reversedTarget);
    }

    @Transactional(readOnly = true)
    public List<FriendCategoryResponse.CategoryInfoDto> getFriendCategories(Long memberId, Long friendId) {
        Friendship friendship = friendManageService.getAcceptedFriendship(memberId, friendId);
        List<Category> friendCategories = friendship.getFriend().getCategories();

        return friendCategories.stream()
                .map(FriendCategoryConverter::toCategoryInfoDto)
                .toList();
    }
}
