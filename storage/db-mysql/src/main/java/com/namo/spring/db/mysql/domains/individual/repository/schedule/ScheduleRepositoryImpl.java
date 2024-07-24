package com.namo.spring.db.mysql.domains.individual.repository.schedule;

import static com.namo.spring.db.mysql.domains.group.domain.QMoimSchedule.*;
import static com.namo.spring.db.mysql.domains.group.domain.QMoimScheduleAndUser.*;
import static com.namo.spring.db.mysql.domains.individual.domain.QCategory.*;
import static com.namo.spring.db.mysql.domains.individual.domain.QImage.*;
import static com.namo.spring.db.mysql.domains.individual.domain.QPalette.*;
import static com.namo.spring.db.mysql.domains.individual.domain.QSchedule.*;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.EntityManager;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.namo.spring.db.mysql.domains.group.type.VisibleStatus;
import com.namo.spring.db.mysql.domains.individual.domain.Schedule;
import com.namo.spring.db.mysql.domains.individual.dto.MoimScheduleProjection;
import com.namo.spring.db.mysql.domains.individual.dto.ScheduleProjection;
import com.namo.spring.db.mysql.domains.user.domain.User;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;

import static com.namo.spring.db.mysql.domains.group.domain.QMoimSchedule.moimSchedule;
import static com.namo.spring.db.mysql.domains.group.domain.QMoimScheduleAlarm.moimScheduleAlarm;
import static com.namo.spring.db.mysql.domains.group.domain.QMoimScheduleAndUser.moimScheduleAndUser;
import static com.namo.spring.db.mysql.domains.individual.domain.QCategory.category;
import static com.namo.spring.db.mysql.domains.individual.domain.QImage.image;
import static com.namo.spring.db.mysql.domains.individual.domain.QPalette.palette;
import static com.namo.spring.db.mysql.domains.individual.domain.QSchedule.schedule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public ScheduleRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public List<ScheduleProjection.ScheduleDto> findPersonalSchedulesByUserId(User user, LocalDateTime startDate,
                                                                              LocalDateTime endDate) {
        return queryFactory
                .select(Projections.constructor(
                        ScheduleProjection.ScheduleDto.class,
                        schedule,
                        category.id
                )).distinct()
                .from(schedule)
                .leftJoin(schedule.category, category)
                .leftJoin(schedule.alarms)
                .where(schedule.user.eq(user),
                        scheduleDateLoe(endDate),
                        scheduleDateGoe(startDate))
                .fetch();
    }

    private BooleanExpression scheduleDateLoe(LocalDateTime endDate) {
        return endDate != null ? schedule.period.startDate.before(endDate) : null;
    }

    private BooleanExpression scheduleDateGoe(LocalDateTime startDate) {
        return startDate != null ? schedule.period.endDate.after(startDate) : null;
    }

    @Override
    public List<MoimScheduleProjection.ScheduleDto> findMoimSchedulesByUserId(
            User user,
            LocalDateTime startDate, LocalDateTime endDate
    ) {
        return queryFactory
                .select(Projections.constructor(MoimScheduleProjection.ScheduleDto.class,
                                moimScheduleAndUser.moimSchedule.id,
                                moimScheduleAndUser.moimSchedule.name,
                                moimScheduleAndUser.moimSchedule.period.startDate,
                                moimScheduleAndUser.moimSchedule.period.endDate,
                                moimScheduleAndUser.moimSchedule.period.dayInterval,
                                moimScheduleAndUser.moimSchedule.location.x,
                                moimScheduleAndUser.moimSchedule.location.y,
                                moimScheduleAndUser.moimSchedule.location.locationName,
                                moimScheduleAndUser.moimSchedule.location.kakaoLocationId,
                                category.id,
                                moimScheduleAndUser.moimSchedule.moimMemo,
                                moimScheduleAndUser
                        )
                )
                .from(moimScheduleAndUser)
                .leftJoin(moimScheduleAndUser.moimSchedule, moimSchedule)
                .leftJoin(moimSchedule.moimMemo)
                .leftJoin(moimScheduleAndUser.category, category)
                .leftJoin(moimScheduleAndUser.moimScheduleAlarms, moimScheduleAlarm)
                .where(
                        moimScheduleAndUser.user.eq(user),
                        moimScheduleDateLoe(endDate),
                        moimScheduleDateGoe(startDate),
                        moimScheduleAndUser.visibleStatus.ne(VisibleStatus.NOT_SEEN_PERSONAL_SCHEDULE)
                ).fetch();
    }

    private BooleanExpression moimScheduleDateGoe(LocalDateTime startDate) {
        return startDate != null ? moimSchedule.period.endDate.after(startDate) : null;
    }

    private BooleanExpression moimScheduleDateLoe(LocalDateTime endDate) {
        return endDate != null ? moimSchedule.period.startDate.before(endDate) : null;
    }

    @Override
    public Slice<ScheduleProjection.DiaryDto> findScheduleDiaryByMonth(User user, LocalDateTime startDate,
                                                                       LocalDateTime endDate, Pageable pageable) {
        List<ScheduleProjection.DiaryDto> content = queryFactory
                .select(Projections.constructor(ScheduleProjection.DiaryDto.class,
                        schedule,
                        schedule.category.id,
                        palette.id))
                .from(schedule)
                .join(schedule.category, category)
                .join(category.palette, palette)
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
        return new SliceImpl<>(content, pageable, hasNext);
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
