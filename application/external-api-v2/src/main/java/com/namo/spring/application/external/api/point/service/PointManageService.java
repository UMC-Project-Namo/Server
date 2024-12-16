package com.namo.spring.application.external.api.point.service;

import org.springframework.stereotype.Service;

import com.namo.spring.application.external.api.point.converter.PointTransactionConverter;
import com.namo.spring.db.mysql.domains.point.entity.Point;
import com.namo.spring.db.mysql.domains.point.entity.PointTransaction;
import com.namo.spring.db.mysql.domains.point.service.PointTransactionService;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointManageService {

    private final PointTransactionService pointTransactionService;

    public void chargeRequest(Member member, Long amount) {
        PointTransaction pointTransaction = PointTransactionConverter.toChargeRequestTransaction(
                member.getPoint(), amount);
        pointTransactionService.save(pointTransaction);
    }

    public void depositPoint(PointTransaction pendingTransaction) {
        Point point = pendingTransaction.getPoint();
        point.add(pendingTransaction.getAmount());
    }
}
