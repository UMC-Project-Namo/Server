package com.example.namo2.domain.group.dao.repository.diary;


import static com.example.namo2.domain.group.domain.QMoimMemoLocation.*;
import static com.example.namo2.domain.group.domain.QMoimMemoLocationAndUser.*;

import java.util.List;

import jakarta.persistence.EntityManager;

import com.example.namo2.domain.group.domain.MoimMemoLocation;
import com.example.namo2.domain.group.domain.MoimMemoLocationAndUser;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.example.namo2.domain.group.domain.MoimSchedule;

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
