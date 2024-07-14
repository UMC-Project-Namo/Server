package com.namo.spring.db.mysql.domains.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
