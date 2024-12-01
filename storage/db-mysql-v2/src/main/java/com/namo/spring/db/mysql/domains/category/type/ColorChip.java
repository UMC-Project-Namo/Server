package com.namo.spring.db.mysql.domains.category.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ColorChip {
    NAMO_ORANGE(1L, "#DA6022", "Namo Orange"), // 모임 기본 컬러
    NAMO_PINK(2L, "#DE8989", "Namo Pink"), // 개인 기본 컬러
    NAMO_YELLOW(3L, "#E1B000", "Namo Yellow"), // 생일 기본 컬러
    NAMO_BLUE(4L, "#5C8596", "Namo Blue"),
    LIGHT_GRAY(5L, "#DADADA", "Light Gray"),
    RED(6L, "#EB5353", "Red"),
    PINK(7L, "#FFA192", "Pink"),
    ORANGE(8L, "#EC9B3B", "Orange"),
    YELLOW(9L, "#FFE70F", "Yellow"),
    LIME(10L, "#B3DF67", "Lime"),
    LIGHT_GREEN(11L, "#78A756", "Light Green"),
    GREEN(12L, "#24794F", "Green"),
    CYAN(13L, "#5AE0BC", "Cyan"),
    LIGHT_BLUE(14L, "#45C1D4", "Light Blue"),
    BLUE(15L, "#355080", "Blue"),
    LAVENDER(16L, "#8571BF", "Lavendar"),
    PURPLE(17L, "#833286", "Purple"),
    MAGENTA(18L, "#FF70DE", "Magenta"),
    DARK_GRAY(19L, "#9C9C9C", "Dark Gray"),
    BLACK(20L, "#1D1D1D", "Black");
    
    private final Long id;
    private final String hexCode;
    private final String name;

    public Long getId() {
        return this.id;
    }

    public static Long getBaseCategoryPaletteId() {
        return NAMO_PINK.id;
    }

    public static Long getBaseMeetingCategoryPaletteId() {
        return NAMO_ORANGE.id;
    }

    public static Long getBaseBirthdayCategoryPaletteId() {
        return NAMO_YELLOW.id;
    }

}
