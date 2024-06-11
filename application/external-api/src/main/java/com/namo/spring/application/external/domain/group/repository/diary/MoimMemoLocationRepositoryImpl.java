package com.namo.spring.application.external.domain.group.repository.diary;

import static com.namo.spring.application.external.domain.group.domain.QMoimMemoLocation.*;

import java.util.List;

import jakarta.persistence.EntityManager;

import com.namo.spring.application.external.domain.group.domain.MoimMemoLocation;
import com.namo.spring.application.external.domain.group.domain.MoimMemoLocationAndUser;
import com.namo.spring.application.external.domain.group.domain.MoimSchedule;
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
