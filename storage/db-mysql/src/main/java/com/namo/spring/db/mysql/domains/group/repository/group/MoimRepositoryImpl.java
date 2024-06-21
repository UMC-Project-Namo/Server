package com.namo.spring.db.mysql.domains.group.repository.group;

import static com.namo.spring.db.mysql.domains.group.domain.QMoim.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;

import com.namo.spring.db.mysql.domains.group.domain.Moim;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class MoimRepositoryImpl implements MoimRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public MoimRepositoryImpl(EntityManager em) {
		queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Moim findMoimHavingLockWithMoimAndUsersByCode(String code) {
		return queryFactory.selectFrom(moim)
			.join(moim.moimAndUsers).fetchJoin()
			.setLockMode(LockModeType.PESSIMISTIC_WRITE)
			.where(moim.code.eq(code))
			.fetchOne();
	}

	@Override
	public Moim findHavingLockById(Long moimId) {
		return queryFactory.selectFrom(moim)
			.join(moim.moimAndUsers).fetchJoin()
			.setLockMode(LockModeType.PESSIMISTIC_WRITE)
			.where(moim.id.eq(moimId))
			.fetchOne();
	}
}
