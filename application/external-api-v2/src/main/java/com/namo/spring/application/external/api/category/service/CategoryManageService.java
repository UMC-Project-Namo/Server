package com.namo.spring.application.external.api.category.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.namo.spring.application.external.api.category.dto.CategoryRequest;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.category.exception.CategoryException;
import com.namo.spring.db.mysql.domains.category.exception.PaletteException;
import com.namo.spring.db.mysql.domains.category.service.CategoryService;
import com.namo.spring.db.mysql.domains.category.service.PaletteService;
import com.namo.spring.db.mysql.domains.category.type.CategoryType;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryManageService {

    private final CategoryMaker categoryMaker;
    private final CategoryService categoryService;
    private final PaletteService paletteService;

    /**
     * 나의 카테고리를 조회하는 메서드입니다.
     * !! 카테고리 존재여부, 나의 카테고리 여부를 검증합니다.
     * @param memberId
     * @param categoryId
     * @return
     */
    public Category getMyCategory(Long memberId, Long categoryId){
        Category category = categoryService.readCategory(categoryId)
                .orElseThrow(() -> new CategoryException(ErrorStatus.NOT_FOUND_CATEGORY_FAILURE));
        if (!category.getMember().getId().equals(memberId))
            throw new CategoryException(ErrorStatus.NOT_USERS_CATEGORY);
        return category;
    }

    public List<Category> getMyCategories(Long memberId){
        return categoryService.readCategoriesByMemberId(memberId);
    }

    public void createCategory(Member member, CategoryRequest.CategoryCreateDto request) {
        categoryMaker.makeCategory(member, request.getCategoryName(), request.getPaletteId(), request.getIsShared());
    }

    public void updateCategory(Category category, CategoryRequest.CategoryUpdateDto updateDto) {
        Palette palette = paletteService.readPalette(updateDto.getPaletteId())
                .orElseThrow(() -> new PaletteException(ErrorStatus.NOT_FOUND_PALETTE_FAILURE));
        category.update(updateDto.getCategoryName(), palette, updateDto.getIsShared());
    }

    /**
     * 카테고리를 삭제하는 메서드입니다.
     * !! 기본 제공 카테고리, 사용중인 카테고리는 삭제가 불가능합니다.
     * @param category
     */
    public void deleteCategory(Category category) {
        if(!category.getType().equals(CategoryType.COMMON))
            throw new CategoryException(ErrorStatus.NOT_DELETE_BASE_CATEGORY_FAILURE);
        if (categoryService.isCategoryInUse(category)) {
            throw new CategoryException(ErrorStatus.CATEGORY_IN_USE_FAILURE);
        }
        categoryService.deleteCategory(category);

    }
}
