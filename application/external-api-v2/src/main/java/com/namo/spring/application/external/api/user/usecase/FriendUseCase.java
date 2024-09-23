package com.namo.spring.application.external.api.user.usecase;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.user.converter.FriendShipConverter;
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
    public void requestFriendship(Long myMemberId, Long targetMemberId) {
        Member me = memberManageService.getMember(myMemberId);
        Member friend = memberManageService.getMember(targetMemberId);
        friendManageService.requestFriendShip(me, friend);
    }

    @Transactional(readOnly = true)
    public List<FriendshipResponse.FriendRequestDto> getFriendshipRequest(Long memberId) {
        List<Friendship> receivedRequests = friendManageService.getReceivedFriendRequests(memberId);
        return receivedRequests.stream()
                .map(FriendShipConverter::toFriendRequestDto)
                .toList();
    }
}
