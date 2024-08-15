package com.namo.spring.db.mysql.domains.category.service;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.category.exception.CategoryException;
import com.namo.spring.db.mysql.domains.category.repository.CategoryRepository;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@DomainService
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public Optional<Category> readCategory(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }


    @Transactional(readOnly = true)
    public Category readBaseCategoryByMember(Member member) {
        return categoryRepository.findBaseCategoryByMember(member).orElseThrow(
                () -> new CategoryException(ErrorStatus.NOT_FOUND_CATEGORY_FAILURE)
        );
    }

    @Transactional(readOnly = true)
    public Category readMeetingCategoryByMember(Member member) {
        return categoryRepository.findMeetingCategoryByMember(member).orElseThrow(
                () -> new CategoryException(ErrorStatus.NOT_FOUND_CATEGORY_FAILURE)
        );
    }

    @Transactional
    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

}
