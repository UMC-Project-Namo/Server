package com.namo.spring.db.mysql.domains.category.service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryRepository categoryRepository;

	@Transactional
	public Category createCategory(Category category) {
		return categoryRepository.save(category);
	}

	@Transactional(readOnly = true)
	public Optional<Category> getCategory(Long categoryId) {
		return categoryRepository.findById(categoryId);
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
