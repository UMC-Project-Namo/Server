package com.namo.spring.db.mysql.domains.group.repository.diary;

import static com.namo.spring.db.mysql.domains.group.domain.QMoimMemoLocation.*;
import static com.namo.spring.db.mysql.domains.group.domain.QMoimMemoLocationAndUser.*;

import java.util.List;

import jakarta.persistence.EntityManager;

import com.namo.spring.db.mysql.domains.group.domain.MoimMemoLocation;
import com.namo.spring.db.mysql.domains.group.domain.MoimMemoLocationAndUser;
import com.namo.spring.db.mysql.domains.group.domain.MoimSchedule;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class MoimMemoLocationRepositoryImpl implements MoimMemoLocationRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public MoimMemoLocationRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<MoimMemoLocation> findMoimMemoLocationsWithImgs(MoimSchedule moimSchedule) {
		return queryFactory.select(moimMemoLocation).distinct()
			.from(moimMemoLocation)
			.join(moimMemoLocation.moimMemo).fetchJoin()
			.leftJoin(moimMemoLocation.moimMemoLocationImgs).fetchJoin()
			.where(moimMemoLocation.moimMemo.moimSchedule.eq(moimSchedule))
			.fetch();
	}

	@Override
	public List<MoimMemoLocationAndUser> findMoimMemoLocationAndUsers(List<MoimMemoLocation> moimMemoLocations) {
		return queryFactory.select(moimMemoLocationAndUser)
			.from(moimMemoLocationAndUser)
			.where(moimMemoLocationAndUser.moimMemoLocation.in(moimMemoLocations))
			.fetch();
	}
}
