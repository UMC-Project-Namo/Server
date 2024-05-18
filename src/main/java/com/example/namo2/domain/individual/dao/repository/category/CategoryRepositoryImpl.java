package com.example.namo2.domain.individual.dao.repository.category;

import static com.example.namo2.domain.individual.domain.QCategory.*;

import java.util.List;

import jakarta.persistence.EntityManager;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.example.namo2.domain.individual.domain.Category;
import com.example.namo2.domain.individual.domain.constant.CategoryKind;

import com.example.namo2.domain.user.domain.User;

public class CategoryRepositoryImpl implements CategoryRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public CategoryRepositoryImpl(EntityManager em) {
		queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<Category> findMoimCategoriesByUsers(List<User> users, CategoryKind kind) {
		return queryFactory
			.selectFrom(category)
			.join(category.user).fetchJoin()
			.where(category.user.in(users),
				category.kind.eq(kind))
			.fetch();
	}
}
