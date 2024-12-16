package com.namo.spring.db.mysql.domains.point.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.namo.spring.db.mysql.domains.point.entity.PointTransaction;
import com.namo.spring.db.mysql.domains.point.enums.TransactionStatus;
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
}
