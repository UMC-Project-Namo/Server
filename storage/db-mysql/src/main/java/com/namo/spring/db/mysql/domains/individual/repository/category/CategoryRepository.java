package com.namo.spring.db.mysql.domains.individual.repository.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.individual.domain.Category;
import com.namo.spring.db.mysql.domains.individual.type.CategoryStatus;

import com.namo.spring.db.mysql.domains.user.domain.User;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {
	List<Category> findCategoriesByUserIdAndStatusEquals(Long userId, CategoryStatus status);

	void deleteAllByUser(User user);
}
