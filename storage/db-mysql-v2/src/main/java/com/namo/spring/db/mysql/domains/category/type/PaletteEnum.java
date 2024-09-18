package com.namo.spring.db.mysql.domains.category.type;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaletteEnum {
    BASIC(ColorCategory.BASIC,
            new ColorChip[] {
                    ColorChip.NAMO_ORANGE,
                    ColorChip.NAMO_PINK,
                    ColorChip.NAMO_YELLOW,
                    ColorChip.NAMO_BLUE,
                    ColorChip.LIGHT_GRAY,
                    ColorChip.RED,
                    ColorChip.PINK,
                    ColorChip.ORANGE,
                    ColorChip.YELLOW,
                    ColorChip.LIME,
                    ColorChip.LIGHT_GREEN,
                    ColorChip.GREEN,
                    ColorChip.CYAN,
                    ColorChip.LIGHT_BLUE,
                    ColorChip.BLUE,
                    ColorChip.LAVENDER,
                    ColorChip.PURPLE,
                    ColorChip.MAGENTA,
                    ColorChip.DARK_GRAY,
                    ColorChip.BLACK
            });
    
    private final ColorCategory category;
    private final ColorChip[] containColors;

    public static ColorChip[] getBasicColors() {
        return BASIC.containColors;
    }

    public static long[] getBasicColorIds() {
        return Arrays.stream(BASIC.containColors)
                .mapToLong(colorChip -> colorChip.getId())
                .toArray();
    }
}
