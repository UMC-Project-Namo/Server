package com.namo.spring.db.mysql.domains.individual.repository.category;

import static com.namo.spring.db.mysql.domains.individual.domain.QCategory.*;

import java.util.List;

import jakarta.persistence.EntityManager;

import com.namo.spring.db.mysql.domains.individual.domain.Category;
import com.namo.spring.db.mysql.domains.individual.type.CategoryKind;
import com.namo.spring.db.mysql.domains.user.domain.User;
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
