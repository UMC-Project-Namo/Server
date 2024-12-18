package com.namo.spring.db.mysql.domains.point.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.namo.spring.db.mysql.domains.point.entity.PointTransaction;
import com.namo.spring.db.mysql.domains.point.enums.TransactionStatus;
import com.namo.spring.db.mysql.domains.point.enums.TransactionType;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    Optional<PointTransaction> findPointTransactionByIdAndTransactionStatus(Long pointTransactionId, TransactionStatus transactionStatus);

    // PENDING 상태의 충전 요청 트랜잭션 조회
    @Query("SELECT pt FROM PointTransaction pt " +
            "WHERE pt.transactionType = :transactionType " +
            "AND pt.transactionStatus = :transactionStatus")
    Page<PointTransaction> findAllByTransactionTypeAndPointStatus(
            @Param("transactionType") TransactionType transactionType,
            @Param("transactionStatus") TransactionStatus transactionStatus,
            Pageable pageable
    );
}
