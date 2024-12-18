package com.namo.spring.application.external.api.point.usecase;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.point.converter.PointTransactionConverter;
import com.namo.spring.application.external.api.point.dto.PointResponse;
import com.namo.spring.application.external.api.point.service.PointManageService;
import com.namo.spring.application.external.api.point.service.PointTransactionManageService;
import com.namo.spring.application.external.api.user.service.MemberManageService;
import com.namo.spring.db.mysql.domains.point.entity.PointTransaction;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PointChargeUseCase {

    private final PointManageService pointManageService;
    private final MemberManageService memberManageService;
    private final PointTransactionManageService pointTransactionManageService;

    @Transactional
    public void requestChargePoints(Long memberId, Long amount) {
        Member member = memberManageService.getActiveMember(memberId);
        pointManageService.chargeRequest(member, amount);
    }

    @Transactional
    public void acceptRequest(Long pointTransactionId) {
        // Transaction 상태 변경
        PointTransaction pendingTransaction = pointTransactionManageService.getPendingTransaction(pointTransactionId);
        pendingTransaction.accept();

        // Point 입금
        pointManageService.depositPoint(pendingTransaction);
    }

    @Transactional(readOnly = true)
    public PointResponse.ChargePointRequestListDto getAllChargeRequests(int page) {
        Page<PointTransaction> requestList = pointTransactionManageService.getAllChargeRequest(page);
        return PointTransactionConverter
                .toChargePointRequestListDto(requestList);
    }
}
