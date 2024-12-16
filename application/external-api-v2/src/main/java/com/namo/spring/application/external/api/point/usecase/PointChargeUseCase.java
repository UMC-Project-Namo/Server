package com.namo.spring.application.external.api.point.usecase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.point.service.PointManageService;
import com.namo.spring.application.external.api.user.service.MemberManageService;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PointChargeUseCase {

    private final PointManageService pointManageService;
    private final MemberManageService memberManageService;

    @Transactional
    public void requestChargePoints(Long memberId, Long amount) {
        Member member = memberManageService.getActiveMember(memberId);
        pointManageService.chargeRequest(member, amount);
    }
}
