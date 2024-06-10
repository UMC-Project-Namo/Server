package com.example.namo2.domain.individual.dao.repository.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.namo2.domain.individual.domain.Category;
import com.example.namo2.domain.individual.domain.constant.CategoryStatus;

import com.example.namo2.domain.user.domain.User;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {
	List<Category> findCategoriesByUserIdAndStatusEquals(Long userId, CategoryStatus status);

	void deleteAllByUser(User user);
}
