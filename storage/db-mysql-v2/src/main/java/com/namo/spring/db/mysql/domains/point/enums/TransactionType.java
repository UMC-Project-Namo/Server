package com.namo.spring.db.mysql.domains.point.enums;

import lombok.Getter;

@Getter
public enum TransactionType {

    CHARGE("충전"),
    SPEND("사용"),
    REFUND("환불"),
    REWARD("보상");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }
}
