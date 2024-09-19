package com.namo.spring.db.mysql.domains.category.type;

import com.fasterxml.jackson.annotation.JsonValue;
import com.namo.spring.db.mysql.common.converter.CodedEnum;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CategoryType implements CodedEnum {
    COMMON("1", "일반"),
    BASE_MEETING("2", "모임"),
    BASE_SCHEDULE("3", "일정"),
    BIRTHDAY("4", "생일");

    private final String code;
    private final String type;

    @Override
    public String toString() {
        return this.type;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    public String getType() {
        return this.type;
    }

    @JsonValue
    public String createJson() {
        return this.name();
    }
}
