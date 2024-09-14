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

    public List<Category> getMyCategories(Long memberId){
        return categoryService.readCategoriesByMemberId(memberId);
    }
}
