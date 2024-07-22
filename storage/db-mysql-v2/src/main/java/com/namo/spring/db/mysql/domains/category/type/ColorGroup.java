package com.namo.spring.db.mysql.domains.category.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ColorGroup {
    BASIC(ColorCategory.BASIC, new ColorChip[]{ColorChip.COLOR_1, ColorChip.COLOR_2, ColorChip.COLOR_3, ColorChip.COLOR_4}),
    PALETTE(ColorCategory.PALETTE, new ColorChip[]{ColorChip.COLOR_5, ColorChip.COLOR_6, ColorChip.COLOR_7, ColorChip.COLOR_8, ColorChip.COLOR_9, ColorChip.COLOR_10, ColorChip.COLOR_11
            , ColorChip.COLOR_12, ColorChip.COLOR_13, ColorChip.COLOR_14});
    private final ColorCategory category;
    private final ColorChip[] containColor;

    public static ColorChip[] getBasicColors() {
        return BASIC.containColor;
    }

    public static ColorChip[] getPaletteColors() {
        return PALETTE.containColor;
    }
}
