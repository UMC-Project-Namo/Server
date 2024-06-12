package com.namo.spring.application.external.domain.individual.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.domain.individual.application.converter.CategoryConverter;
import com.namo.spring.application.external.domain.individual.application.converter.CategoryResponseConverter;
import com.namo.spring.application.external.domain.individual.application.impl.CategoryService;
import com.namo.spring.application.external.domain.individual.application.impl.PaletteService;
import com.namo.spring.application.external.domain.individual.domain.Category;
import com.namo.spring.application.external.domain.individual.domain.Palette;
import com.namo.spring.application.external.domain.individual.ui.dto.CategoryRequest;
import com.namo.spring.application.external.domain.individual.ui.dto.CategoryResponse;

import com.namo.spring.application.external.domain.user.application.impl.UserService;
import com.namo.spring.application.external.domain.user.domain.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryFacade {
	private final CategoryService categoryService;
	private final PaletteService paletteService;
	private final UserService userService;

	@Transactional(readOnly = false)
	public CategoryResponse.CategoryIdDto create(Long userId, CategoryRequest.PostCategoryDto dto) {
		User user = userService.getUser(userId);
		Palette palette = paletteService.getPalette(dto.getPaletteId());
		Category category = CategoryConverter.toCategory(dto, user, palette);
		Category savedCategory = categoryService.create(category);

		return CategoryResponseConverter.toCategoryIdDto(savedCategory);
	}

	@Transactional(readOnly = true)
	public List<CategoryResponse.CategoryDto> getCategories(Long userId) {
		List<Category> categories = categoryService.getCategories(userId);

		return CategoryResponseConverter.toCategoryDtoList(categories);
	}

	@Transactional(readOnly = false)
	public CategoryResponse.CategoryIdDto modifyCategory(Long categoryId, CategoryRequest.PostCategoryDto dto,
		Long userId) {
		Palette palette = paletteService.getPalette(dto.getPaletteId());
		Category modifiedCategory = categoryService.modifyCategory(categoryId, dto, palette, userId);

		return CategoryResponseConverter.toCategoryIdDto(modifiedCategory);
	}

	@Transactional(readOnly = false)
	public void deleteCategory(Long categoryId, Long userId) {
		categoryService.delete(categoryId, userId);
	}
}
