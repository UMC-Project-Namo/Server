package com.namo.spring.application.external.api.category.usecase;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.category.converter.CategoryConverter;
import com.namo.spring.application.external.api.category.dto.CategoryResponse;
import com.namo.spring.application.external.api.category.service.CategoryManageService;
import com.namo.spring.db.mysql.domains.category.entity.Category;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryUseCase {

    private final CategoryManageService categoryManageService;

    @Transactional(readOnly = true)
    public List<CategoryResponse.MyCategoryInfoDto> getMyCategoryList(Long memberId){
        List<Category> myCategories = categoryManageService.getMyCategories(memberId);
        return myCategories.stream()
                .map(CategoryConverter::toMyCategoryInfoDto)
                .toList();
    }

}
