package com.example.namo2.domain.group.dao.repository.schedule;

import static com.example.namo2.domain.group.domain.QMoimSchedule.*;
import static com.example.namo2.domain.group.domain.QMoimScheduleAndUser.*;
import static com.example.namo2.domain.individual.domain.QCategory.*;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.EntityManager;

import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.example.namo2.domain.group.domain.MoimScheduleAndUser;
import com.example.namo2.domain.group.domain.constant.VisibleStatus;

import com.example.namo2.domain.user.domain.User;

public class MoimScheduleAndUserRepositoryImpl implements MoimScheduleAndUserRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public MoimScheduleAndUserRepositoryImpl(EntityManager em) {
		queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<MoimScheduleAndUser> findMoimScheduleAndUserWithMoimScheduleByUsersAndDates(LocalDateTime startDate,
		LocalDateTime endDate, List<User> users) {
		return queryFactory.select(moimScheduleAndUser)
			.from(moimScheduleAndUser)
			.join(moimScheduleAndUser.moimSchedule, moimSchedule).fetchJoin()
			.join(moimScheduleAndUser.category, category).fetchJoin()
			.leftJoin(moimSchedule.moimMemo).fetchJoin()
			.where(moimScheduleAndUser.user.in(users),
				moimScheduleDateGoe(startDate),
				moimScheduleDateLoe(endDate),
				moimScheduleAndUser.category.share.isTrue()
			)
			.fetch();
	}

	private BooleanExpression moimScheduleDateGoe(LocalDateTime startDate) {
		return startDate != null ? moimSchedule.period.endDate.after(startDate) : null;
	}

	private BooleanExpression moimScheduleDateLoe(LocalDateTime endDate) {
		return endDate != null ? moimSchedule.period.startDate.before(endDate) : null;
	}

	/**
	 * 이 부분에 대해서는 MoimScheduleAndUser를 반환해서
	 * 우선 MoimScheduleAndUserRepository로
	 * 넘기기는 했는데 MoimMemo에 가까운 로직이라 이곳으로 들어오는게 맞나 싶네요?
	 */
	@Override
	public List<MoimScheduleAndUser> findMoimScheduleMemoByMonthPaging(User user, List<LocalDateTime> dates,
		Pageable pageable) {
		return queryFactory.select(moimScheduleAndUser)
			.from(moimScheduleAndUser)
			.join(moimScheduleAndUser.moimSchedule, moimSchedule).fetchJoin()
			.join(moimSchedule.moimMemo).fetchJoin()
			.join(moimScheduleAndUser.category, category).fetchJoin()
			.join(category.palette).fetchJoin()
			.where(moimSchedule.period.startDate.before(dates.get(1)),
				moimSchedule.period.endDate.after(dates.get(0)),
				moimScheduleAndUser.user.eq(user),
				moimScheduleAndUser.visibleStatus.eq(VisibleStatus.ALL)
			)
			.orderBy(moimSchedule.period.startDate.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();
	}
}
