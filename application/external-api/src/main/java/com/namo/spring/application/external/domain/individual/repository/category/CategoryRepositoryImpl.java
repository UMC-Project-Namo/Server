package com.namo.spring.application.external.domain.individual.repository.category;

import static com.namo.spring.application.external.domain.individual.domain.QCategory.*;

import java.util.List;

import jakarta.persistence.EntityManager;

import com.namo.spring.application.external.domain.individual.domain.Category;
import com.namo.spring.application.external.domain.individual.domain.constant.CategoryKind;
import com.namo.spring.application.external.domain.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

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
