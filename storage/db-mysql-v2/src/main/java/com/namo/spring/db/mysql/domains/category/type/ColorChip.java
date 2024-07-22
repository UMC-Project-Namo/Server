package com.namo.spring.db.mysql.domains.category.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ColorChip {
    COLOR_1(1, ""),
    COLOR_2(2, ""),
    COLOR_3(3, ""),
    COLOR_4(4, ""),
    COLOR_5(5, ""),
    COLOR_6(7, ""),
    COLOR_7(8, ""),
    COLOR_8(9, ""),
    COLOR_9(10, ""),
    COLOR_10(11, ""),
    COLOR_11(12, ""),
    COLOR_12(13, ""),
    COLOR_13(14, ""),
    COLOR_14(15, "");

    private final Integer code;
    private final String colorCode;

    public Integer getCode() {
        return this.code;
    }
}
