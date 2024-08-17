package com.namo.spring.db.mysql.domains.category.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ColorCategory {
    BASIC("1", "기본"),
    PALETTE("2", "팔레트");

    private final String code;
    private final String viewName;
}
