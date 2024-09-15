package com.namo.spring.application.external.api.category.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.category.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryManageService {

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
}
