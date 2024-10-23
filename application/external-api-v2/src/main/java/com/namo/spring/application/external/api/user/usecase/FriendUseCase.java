package com.namo.spring.application.external.api.user.usecase;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.user.converter.FriendshipConverter;
import com.namo.spring.application.external.api.user.dto.FriendshipResponse;
import com.namo.spring.application.external.api.user.service.FriendManageService;
import com.namo.spring.application.external.api.user.service.MemberManageService;
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
    public FriendshipResponse.FriendListDto getFriendList(Long userId, int page) {
        Page<Friendship> friendships = friendManageService.getAcceptedFriendship(userId, page);
        return FriendshipConverter.toFriendListDto(friendships);
    }
}
