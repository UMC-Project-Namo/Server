package com.namo.spring.db.mysql.domains.point.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.point.exception.PointException;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Long balance;

    @Builder
    public Point(Member member){
        this.balance = 0L;
        this.member = member;
    }

    public static Point createInitialPoint(Member member) {
        return Point.builder()
                .member(member)
                .build();
    }

    public void add(Long amount) {
        if (amount <= 0) {
            throw new PointException(ErrorStatus.INVALID_AMOUNT);
        }
        this.balance += amount;
    }

    public void subtract(Long amount) {
        if (amount <= 0) {
            throw new PointException(ErrorStatus.INVALID_AMOUNT);
        }
        if (this.balance < amount) {
            throw new PointException(ErrorStatus.POINT_UNDER_ZERO);
        }
        this.balance -= amount;
    }
}
