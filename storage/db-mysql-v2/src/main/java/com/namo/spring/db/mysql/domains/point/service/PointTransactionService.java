package com.namo.spring.db.mysql.domains.point.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.namo.spring.db.mysql.domains.point.entity.PointTransaction;
import com.namo.spring.db.mysql.domains.point.enums.TransactionStatus;
import com.namo.spring.db.mysql.domains.point.enums.TransactionType;
import com.namo.spring.db.mysql.domains.point.repository.PointTransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointTransactionService {

    private final PointTransactionRepository repository;

    public void save(PointTransaction pointTransaction) {
        repository.save(pointTransaction);
    }

    public Optional<PointTransaction> readTransaction(Long pointTransactionId, TransactionStatus transactionStatus) {
        return repository.findPointTransactionByIdAndTransactionStatus(pointTransactionId, transactionStatus);
    }

    // 충전 요청 대기 목록 조회 by page
    public Page<PointTransaction> getAllChargeRequest(Pageable pageable) {
        return repository.findAllByTransactionTypeAndPointStatus(TransactionType.CHARGE, TransactionStatus.PENDING, pageable);
    }
}
