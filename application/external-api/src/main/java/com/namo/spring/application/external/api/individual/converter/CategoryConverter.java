package com.namo.spring.application.external.api.individual.converter;

import com.namo.spring.application.external.api.individual.dto.CategoryRequest;
import com.namo.spring.db.mysql.domains.individual.domain.Category;
import com.namo.spring.db.mysql.domains.individual.domain.Palette;
import com.namo.spring.db.mysql.domains.individual.type.CategoryKind;
import com.namo.spring.db.mysql.domains.user.domain.User;

public class CategoryConverter {

	private CategoryConverter() {
		throw new IllegalStateException("Utility class");
	}

	public static Category toCategory(
		CategoryRequest.PostCategoryDto dto,
		User user,
		Palette palette
	) {
		return Category.builder()
			.name(dto.getName())
			.user(user)
			.palette(palette)
			.share(dto.isShare())
			.kind(CategoryKind.CUSTOM)
			.build();
	}

	public static Category toCategory(
		String name,
		Palette palette,
		Boolean isShare,
		User user,
		CategoryKind kind
	) {
		return Category.builder()
			.name(name)
			.palette(palette)
			.share(isShare)
			.user(user)
			.kind(kind)
			.build();
	}
}
