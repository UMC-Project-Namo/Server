package com.namo.spring.application.external.api.user.usecase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.user.service.FriendManageService;
import com.namo.spring.application.external.api.user.service.MemberManageService;
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
}
