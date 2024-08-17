package com.namo.spring.application.external.api.category.service;

import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.category.service.CategoryService;
import com.namo.spring.db.mysql.domains.category.service.PaletteService;
import com.namo.spring.db.mysql.domains.category.type.CategoryStatus;
import com.namo.spring.db.mysql.domains.category.type.CategoryType;
import com.namo.spring.db.mysql.domains.category.type.ColorChip;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CategoryMaker {

    private static final Long BASE_PERSONAL_PALETTE_ID = ColorChip.getBaseCategoryPaletteId();
    private static final Long BASE_MEETING_PALETTE_ID = ColorChip.getBaseMeetingCategoryPaletteId();

    private final PaletteService paletteService;
    private final CategoryService categoryService;

    public void makePersonalCategory(Member member) {
        Palette palette = paletteService.getPalette(BASE_PERSONAL_PALETTE_ID);

        Category target = Category.builder()
                .member(member)
                .palette(palette)
                .name(CategoryType.BASE_SCHEDULE.getType())
                .type(CategoryType.BASE_SCHEDULE)
                .orderNumber(1)
                .status(CategoryStatus.ACTIVE)
                .build();

        categoryService.createCategory(target);
    }

    public void makeMeetingCategory(Member member) {
        Palette palette = paletteService.getPalette(BASE_MEETING_PALETTE_ID);

        Category target = Category.builder()
                .member(member)
                .palette(palette)
                .name(CategoryType.BASE_MEETING.getType())
                .type(CategoryType.BASE_MEETING)
                .orderNumber(2)
                .status(CategoryStatus.ACTIVE)
                .build();

        categoryService.createCategory(target);
    }

}
