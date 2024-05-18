package com.example.namo2.domain.individual.dao.repository.schedule;

import static com.example.namo2.domain.group.domain.QMoimSchedule.*;
import static com.example.namo2.domain.group.domain.QMoimScheduleAndUser.*;
import static com.example.namo2.domain.individual.domain.QCategory.*;
import static com.example.namo2.domain.individual.domain.QImage.*;
import static com.example.namo2.domain.individual.domain.QPalette.*;
import static com.example.namo2.domain.individual.domain.QSchedule.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.example.namo2.domain.group.domain.MoimScheduleAndUser;
import com.example.namo2.domain.group.domain.constant.VisibleStatus;

import com.example.namo2.domain.individual.application.converter.DiaryResponseConverter;
import com.example.namo2.domain.individual.application.converter.ScheduleResponseConverter;
import com.example.namo2.domain.individual.domain.Schedule;
import com.example.namo2.domain.individual.ui.dto.DiaryResponse;
import com.example.namo2.domain.individual.ui.dto.ScheduleResponse;

import com.example.namo2.domain.user.domain.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	private final EntityManager em;

	public ScheduleRepositoryImpl(EntityManager em) {
		queryFactory = new JPAQueryFactory(em);
		this.em = em;
	}

	/**
	 * 알람을 한 번에 가지고 오는 편이 성능상 더 좋은 퍼포먼스를 발휘할지도?
	 */
	@Override
	public List<ScheduleResponse.GetScheduleDto> findSchedulesByUserId(User user, LocalDateTime startDate,
		LocalDateTime endDate) {
		List<ScheduleResponse.GetScheduleDto> results = findPersonalSchedulesByUserId(user, startDate, endDate);
		List<ScheduleResponse.GetScheduleDto> moimSchedules = findMoimSchedulesByUserId(user, startDate, endDate);
		if (moimSchedules != null) {
			results.addAll(moimSchedules);
		}
		return results;
	}

	public List<ScheduleResponse.GetScheduleDto> findPersonalSchedulesByUserId(User user, LocalDateTime startDate,
		LocalDateTime endDate) {
		List<Schedule> schedules = queryFactory
			.select(schedule).distinct()
			.from(schedule)
			.leftJoin(schedule.alarms).fetchJoin()
			.where(schedule.user.eq(user),
				scheduleDateLoe(endDate),
				scheduleDateGoe(startDate))
			.fetch();
		return schedules.stream().map(schedule -> ScheduleResponseConverter.toGetScheduleRes(schedule))
			.collect(Collectors.toList());
	}

	private BooleanExpression scheduleDateLoe(LocalDateTime endDate) {
		return endDate != null ? schedule.period.startDate.before(endDate) : null;
	}

	private BooleanExpression scheduleDateGoe(LocalDateTime startDate) {
		return startDate != null ? schedule.period.endDate.after(startDate) : null;
	}

	@Override
	public List<ScheduleResponse.GetScheduleDto> findMoimSchedulesByUserId(
		User user,
		LocalDateTime startDate, LocalDateTime endDate
	) {
		List<MoimScheduleAndUser> moimScheduleAndUsers = queryFactory
			.select(moimScheduleAndUser).distinct()
			.from(moimScheduleAndUser)
			.join(moimScheduleAndUser.moimSchedule, moimSchedule).fetchJoin()
			.leftJoin(moimScheduleAndUser.moimScheduleAlarms).fetchJoin()
			.leftJoin(moimSchedule.moimMemo).fetchJoin()
			.where(
				moimScheduleAndUser.user.eq(user),
				moimScheduleDateLoe(endDate),
				moimScheduleDateGoe(startDate),
				moimScheduleAndUser.visibleStatus.ne(VisibleStatus.NOT_SEEN_PERSONAL_SCHEDULE)
			).fetch();

		return moimScheduleAndUsers.stream()
			.filter(schedule -> schedule.getVisibleStatus() != VisibleStatus.NOT_SEEN_PERSONAL_SCHEDULE)
			.map(ScheduleResponseConverter::toGetScheduleRes)
			.toList();
	}

	private BooleanExpression moimScheduleDateGoe(LocalDateTime startDate) {
		return startDate != null ? moimSchedule.period.endDate.after(startDate) : null;
	}

	private BooleanExpression moimScheduleDateLoe(LocalDateTime endDate) {
		return endDate != null ? moimSchedule.period.startDate.before(endDate) : null;
	}

	@Override
	public DiaryResponse.SliceDiaryDto findScheduleDiaryByMonthDto(User user, LocalDateTime startDate,
		LocalDateTime endDate, Pageable pageable) {
		List<Schedule> content = queryFactory
			.select(schedule)
			.from(schedule)
			.join(schedule.category, category).fetchJoin()
			.join(category.palette, palette).fetchJoin()
			.where(schedule.user.eq(user)
				.and(schedule.period.startDate.before(endDate)
					.and(schedule.period.endDate.after(startDate))
					.and(schedule.hasDiary.isTrue())
				))
			.orderBy(schedule.period.startDate.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();
		boolean hasNext = false;
		if (content.size() > pageable.getPageSize()) {
			content.remove(pageable.getPageSize());
			hasNext = true;
		}
		SliceImpl<Schedule> schedules = new SliceImpl<>(content, pageable, hasNext);
		Slice<ScheduleResponse.DiaryDto> diarySlices = schedules.map(ScheduleResponseConverter::toDiaryDto);
		return DiaryResponseConverter.toSliceDiaryDto(diarySlices);
	}

	@Override
	public List<DiaryResponse.GetDiaryByUserDto> findAllScheduleDiary(User user) {
		List<Schedule> schedules = queryFactory
			.select(schedule).distinct()
			.from(schedule)
			.leftJoin(schedule.images, image).fetchJoin()
			.where(schedule.user.eq(user),
				schedule.hasDiary.isTrue()
			)
			.fetch();
		return schedules.stream().map(DiaryResponseConverter::toGetDiaryByUserRes).toList();
	}

	@Override
	public Schedule findOneScheduleAndImages(Long scheduleId) {
		return queryFactory
			.select(schedule)
			.distinct()
			.from(schedule)
			.leftJoin(schedule.images, image).fetchJoin()
			.where(schedule.id.eq(scheduleId))
			.fetchOne();
	}
}
