 package com.namo.spring.db.mysql.domains.individual.repository.schedule;

import com.namo.spring.db.mysql.domains.individual.domain.Schedule;
import com.namo.spring.db.mysql.domains.individual.dto.DiaryResponse;
import com.namo.spring.db.mysql.domains.individual.dto.ScheduleResponse;
import com.namo.spring.db.mysql.domains.user.domain.User;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static com.namo.spring.db.mysql.domains.individual.domain.QCategory.category;
import static com.namo.spring.db.mysql.domains.individual.domain.QImage.image;
import static com.namo.spring.db.mysql.domains.individual.domain.QPalette.palette;
import static com.namo.spring.db.mysql.domains.individual.domain.QSchedule.schedule;

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
        return queryFactory
                .select(Projections.constructor(
                        ScheduleResponse.GetScheduleDto.class,
                        schedule.id,
                        schedule.name,
                        schedule.period.startDate,
                        schedule.period.endDate,
                        schedule.alarms,
                        schedule.period.dayInterval,
                        schedule.location.x,
                        schedule.location.y,
                        schedule.location.locationName,
                        schedule.location.kakaoLocationId,
                        schedule.category.id,
                        schedule.hasDiary
                )).distinct()
                .from(schedule)
                .leftJoin(schedule.alarms).fetchJoin()
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
    public Slice<ScheduleResponse.DiaryDto> findScheduleDiaryByMonthDto(User user, LocalDateTime startDate,
                                                                        LocalDateTime endDate, Pageable pageable) {
        List<ScheduleResponse.DiaryDto> content = queryFactory
                .select(Projections.constructor(ScheduleResponse.DiaryDto.class,
                        schedule.id,
                        schedule.name,
                        schedule.period.startDate,
                        schedule.contents,
                        schedule.category.id,
                        schedule.images,
                        schedule.category.palette.id,
                        schedule.location.locationName,
                        schedule.images))
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
        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public List<DiaryResponse.GetDiaryByUserDto> findAllScheduleDiary(User user) {
        return queryFactory
                .select(Projections.constructor(DiaryResponse.GetDiaryByUserDto.class,
                        schedule.id,
                        schedule.contents,
                        schedule.images
                        )).distinct()
                .from(schedule)
                .leftJoin(schedule.images, image).fetchJoin()
                .where(schedule.user.eq(user),
                        schedule.hasDiary.isTrue()
                )
                .fetch();
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
