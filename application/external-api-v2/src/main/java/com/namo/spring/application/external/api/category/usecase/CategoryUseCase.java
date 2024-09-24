package com.namo.spring.application.external.api.category.usecase;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.category.converter.CategoryConverter;
import com.namo.spring.application.external.api.category.dto.CategoryRequest;
import com.namo.spring.application.external.api.category.dto.CategoryResponse;
import com.namo.spring.application.external.api.category.service.CategoryManageService;
import com.namo.spring.application.external.api.user.service.MemberManageService;
import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryUseCase {

    private final CategoryManageService categoryManageService;
    private final MemberManageService memberManageService;

    @Transactional(readOnly = true)
    public List<CategoryResponse.MyCategoryInfoDto> getMyCategoryList(Long memberId){
        List<Category> myCategories = categoryManageService.getMyCategories(memberId);
        return myCategories.stream()
                .map(CategoryConverter::toMyCategoryInfoDto)
                .toList();
    }

    @Transactional
    public void createCategory(Long memberId, CategoryRequest.CategoryCreateDto request){
        Member member = memberManageService.getActiveMember(memberId);
        categoryManageService.createCategory(member, request);
    }

    @Transactional
    public void updateCategory(Long memberId, Long categoryId, CategoryRequest.CategoryUpdateDto request){
        Category category = categoryManageService.getMyCategory(memberId, categoryId);
        categoryManageService.updateCategory(category, request);
    }

    @Transactional
    public void deleteCategory(Long memberId, Long categoryId){
        Category category = categoryManageService.getMyCategory(memberId, categoryId);
        categoryManageService.deleteCategory(category);
    }

}
