package com.namo.spring.application.external.domain.individual.repository.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.application.external.domain.individual.domain.Category;
import com.namo.spring.application.external.domain.individual.domain.constant.CategoryStatus;

import com.namo.spring.db.mysql.domains.user.domain.User;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {
	List<Category> findCategoriesByUserIdAndStatusEquals(Long userId, CategoryStatus status);

	void deleteAllByUser(User user);
}
