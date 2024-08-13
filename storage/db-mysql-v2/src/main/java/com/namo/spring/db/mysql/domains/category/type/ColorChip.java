package com.namo.spring.db.mysql.domains.category.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ColorChip {
    COLOR_1(1L, "2131034732"),  // 개인 카테고리 기본 컬러
    COLOR_2(2L, "2131034733"),
    COLOR_3(3L, "2131034735"),
    COLOR_4(4L, "2131034734"),  // 모임 카테고리 기본 컬러
    COLOR_5(5L, "2131034708"),
    COLOR_6(6L, "2131034710"),
    COLOR_7(7L, "2131034711"),
    COLOR_8(8L, "2131034712"),
    COLOR_9(9L, "2131034713"),
    COLOR_10(10L, "2131034714"),
    COLOR_11(11L, "2131034715"),
    COLOR_12(12L, "2131034716"),
    COLOR_13(13L, "2131034717"),
    COLOR_14(14L, "2131034709");

    private final Long id;
    private final String colorCode;

    public Long getId() {
        return this.id;
    }

    public static Long getBasePersonalCategoryColorId() {
        return COLOR_1.id;
    }

    public static Long getBaseMeetingCategoryColorId() {
        return COLOR_4.id;
    }
}
