package com.namo.spring.db.mysql.domains.point.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.point.entity.PointTransaction;
import com.namo.spring.db.mysql.domains.point.enums.TransactionStatus;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    Optional<PointTransaction> findPointTransactionByIdAndTransactionStatus(Long pointTransactionId, TransactionStatus transactionStatus);
}
