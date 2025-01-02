package com.namo.spring.db.mysql.domains.shop.enums;

public enum ThemeStatus {
    ACTIVE, // 판매중
    INACTIVE, // 판매중지
    DELETED, // 삭제처리 (soft delete)
    PENDING // 판매대기
}
