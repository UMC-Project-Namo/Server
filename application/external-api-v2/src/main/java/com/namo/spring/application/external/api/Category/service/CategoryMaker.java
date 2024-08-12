package com.namo.spring.application.external.api.Category.service;

import org.springframework.stereotype.Component;

import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.category.service.CategoryService;
import com.namo.spring.db.mysql.domains.category.service.PaletteService;
import com.namo.spring.db.mysql.domains.category.type.CategoryStatus;
import com.namo.spring.db.mysql.domains.category.type.CategoryType;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryMaker {

	private static final Long BASE_INDIVIDUAL_PALETTE_ID = 1L;
	private static final Long BASE_GROUP_PALETTE_ID = 4L;

	private final PaletteService paletteService;
	private final CategoryService categoryService;

	public void makeIndividualCategory(Member member) {
		Palette palette = paletteService.getPalette(BASE_INDIVIDUAL_PALETTE_ID);

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

	public void makeGroupCategory(Member member) {
		Palette palette = paletteService.getPalette(BASE_GROUP_PALETTE_ID);

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
