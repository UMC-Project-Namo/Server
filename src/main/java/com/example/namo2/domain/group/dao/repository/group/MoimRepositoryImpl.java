package com.example.namo2.domain.group.dao.repository.group;

import static com.example.namo2.domain.group.domain.QMoim.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.example.namo2.domain.group.domain.Moim;

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
