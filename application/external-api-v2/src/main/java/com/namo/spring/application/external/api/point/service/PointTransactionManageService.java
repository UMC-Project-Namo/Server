package com.namo.spring.application.external.api.point.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.point.entity.PointTransaction;
import com.namo.spring.db.mysql.domains.point.enums.TransactionStatus;
import com.namo.spring.db.mysql.domains.point.exception.PointException;
import com.namo.spring.db.mysql.domains.point.service.PointTransactionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointTransactionManageService {

    private static final Integer REQUEST_PAGE_SIZE = 20;

    private final PointTransactionService pointTransactionService;

    public PointTransaction getPendingTransaction(Long pointTransactionId) {
        return pointTransactionService.readTransaction(pointTransactionId,
                        TransactionStatus.PENDING)
                .orElseThrow(() -> new PointException(ErrorStatus.NOT_FOUND_POINT_REQUEST));
    }

    public Page<PointTransaction> getAllChargeRequest(int page) {
        Pageable pageable = PageRequest.of(page - 1, REQUEST_PAGE_SIZE);
        return pointTransactionService.getAllChargeRequest(pageable);
    }
}
