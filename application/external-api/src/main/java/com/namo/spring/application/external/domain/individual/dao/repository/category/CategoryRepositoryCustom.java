package com.example.namo2.domain.individual.dao.repository.category;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.example.namo2.domain.individual.domain.Category;
import com.example.namo2.domain.individual.domain.constant.CategoryKind;

import com.example.namo2.domain.user.domain.User;

public interface CategoryRepositoryCustom {
	List<Category> findMoimCategoriesByUsers(@Param("users") List<User> users, @Param("kind") CategoryKind kind);
}
