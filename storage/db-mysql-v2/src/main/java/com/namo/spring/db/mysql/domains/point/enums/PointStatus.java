package com.namo.spring.db.mysql.domains.point.enums;

import lombok.Getter;

@Getter
public enum PointStatus {
    FAILED("실패"),
    PENDING("대기 중"),
    CANCELED("취소"),
    ACCEPTED("처리 완료");

    private final String description;

    PointStatus(String description) {
        this.description = description;
    }
}
