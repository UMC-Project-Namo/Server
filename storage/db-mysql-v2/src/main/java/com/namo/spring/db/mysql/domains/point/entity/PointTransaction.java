package com.namo.spring.db.mysql.domains.point.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.point.enums.TransactionStatus;
import com.namo.spring.db.mysql.domains.point.enums.TransactionType;
import com.namo.spring.db.mysql.domains.point.exception.PointException;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Point point;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionStatus transactionStatus;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime transactionDate;

    @Column(length = 255)
    private String description; // 거래 설명

    @Builder
    public PointTransaction(Point point, TransactionType transactionType, Long amount, String description, TransactionStatus transactionStatus) {
        if (amount <= 0) {
            throw new PointException(ErrorStatus.INVALID_AMOUNT);
        }
        this.point = point;
        this.transactionType = transactionType;
        this.transactionStatus = transactionStatus;
        this.amount = amount;
        this.transactionDate = LocalDateTime.now();
        this.description = description;
    }

    public void accept() {
        this.transactionStatus = TransactionStatus.ACCEPTED;
    }
}
