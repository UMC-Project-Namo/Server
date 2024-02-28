package com.example.namo2.domain.schedule.dao.impl;

import static com.example.namo2.domain.category.domain.QCategory.*;
import static com.example.namo2.domain.category.domain.QPalette.*;
import static com.example.namo2.domain.moim.domain.QMoimAndUser.*;
import static com.example.namo2.domain.moim.domain.QMoimSchedule.*;
import static com.example.namo2.domain.moim.domain.QMoimScheduleAndUser.*;
import static com.example.namo2.domain.schedule.domain.QImage.*;
import static com.example.namo2.domain.schedule.domain.QSchedule.*;
import static com.example.namo2.domain.user.domain.QUser.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.example.namo2.domain.moim.application.converter.MoimScheduleResponseConverter;
import com.example.namo2.domain.moim.domain.MoimAndUser;
import com.example.namo2.domain.moim.domain.MoimSchedule;
import com.example.namo2.domain.moim.domain.MoimScheduleAndUser;
import com.example.namo2.domain.moim.ui.dto.MoimScheduleResponse;
import com.example.namo2.domain.schedule.application.converter.ScheduleResponseConverter;
import com.example.namo2.domain.schedule.dao.repository.ScheduleRepositoryCustom;
import com.example.namo2.domain.schedule.domain.Schedule;
import com.example.namo2.domain.schedule.ui.dto.ScheduleResponse;
import com.example.namo2.domain.user.domain.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
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
    public List<ScheduleResponse.GetScheduleDto> findSchedulesByUserId(User user, LocalDateTime startDate, LocalDateTime endDate) {
        List<ScheduleResponse.GetScheduleDto> results = findPersonalSchedulesByUserId(user, startDate, endDate);
        List<ScheduleResponse.GetScheduleDto> moimSchedules = findMoimSchedulesByUserId(user, startDate, endDate);
        if (moimSchedules != null) {
            results.addAll(moimSchedules);
        }
        return results;
    }

    public List<ScheduleResponse.GetScheduleDto> findPersonalSchedulesByUserId(User user, LocalDateTime startDate, LocalDateTime endDate) {
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
    public List<ScheduleResponse.GetScheduleDto> findMoimSchedulesByUserId(User user, LocalDateTime startDate, LocalDateTime endDate) {
        List<MoimScheduleAndUser> moimScheduleAndUsers = queryFactory
                .select(moimScheduleAndUser).distinct()
                .from(moimScheduleAndUser)
                .join(moimScheduleAndUser.moimSchedule, moimSchedule).fetchJoin()
                .leftJoin(moimScheduleAndUser.moimScheduleAlarms).fetchJoin()
                .leftJoin(moimSchedule.moimMemo).fetchJoin()
                .where(moimScheduleAndUser.user.eq(user),
                        moimScheduleDateLoe(endDate),
                        moimScheduleDateGoe(startDate)
                )
                .fetch();
        return moimScheduleAndUsers.stream()
                .map(ScheduleResponseConverter::toGetScheduleRes).collect(Collectors.toList());
    }

    private BooleanExpression moimScheduleDateGoe(LocalDateTime startDate) {
        return startDate != null ? moimSchedule.period.endDate.after(startDate) : null;
    }

    private BooleanExpression moimScheduleDateLoe(LocalDateTime endDate) {
        return endDate != null ? moimSchedule.period.startDate.before(endDate) : null;
    }

    @Override
    public ScheduleResponse.SliceDiaryDto findScheduleDiaryByMonthDto(User user, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
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
        return ScheduleResponseConverter.toSliceDiaryDto(diarySlices);
    }

    @Override
    public List<ScheduleResponse.GetDiaryByUserDto> findAllScheduleDiary(User user) {
        List<Schedule> schedules = queryFactory
            .select(schedule).distinct()
            .from(schedule)
            .leftJoin(schedule.images, image).fetchJoin()
            .where(schedule.user.eq(user),
                schedule.hasDiary.isTrue()
            )
            .fetch();
        return schedules.stream().map(ScheduleResponseConverter::toGetDiaryByUserRes).toList();
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

    // TODO: Moim 관련된 스케줄을 가져오는 것이므로, MoimScheduleRepository로 옮기는 것이 좋을 것 같다.
    @Override
    public List<MoimScheduleResponse.MoimScheduleDto> findMonthScheduleInMoim(Long moimId, LocalDateTime startDate, LocalDateTime endDate) {
        // 모임에 속한 유저들 파악
        List<MoimAndUser> moimAndUsers = queryFactory.select(moimAndUser)
                .from(moimAndUser)
                .join(moimAndUser.user).fetchJoin()
                .where(moimAndUser.moim.id.eq(moimId))
                .fetch();
        List<User> users = moimAndUsers.stream()
                .map((moimAndUser -> moimAndUser.getUser()))
                .collect(Collectors.toList());
        Map<Long, MoimScheduleResponse.MoimScheduleUserDto> moimAndUserDtoMap = moimAndUsers.stream()
                .collect(Collectors.toMap(
                        (moimAndUser -> moimAndUser.getUser().getId()),
                        (moimAndUser -> new MoimScheduleResponse.MoimScheduleUserDto(moimAndUser.getUser().getId(), moimAndUser.getUser().getName(), moimAndUser.getColor()))
                ));

        List<MoimScheduleResponse.MoimScheduleDto> results = findIndivisualSchedule(moimId, startDate, endDate, users);
        Map<MoimSchedule, List<MoimScheduleResponse.MoimScheduleUserDto>> groupScheduleMap = findMoimScheduleAndScheduleUserMap(startDate, endDate, users, moimAndUserDtoMap);

        for (MoimSchedule moimSchedule : groupScheduleMap.keySet()) {
            List<MoimScheduleResponse.MoimScheduleUserDto> moimScheduleUserDtos = groupScheduleMap.get(moimSchedule);
            MoimScheduleResponse.MoimScheduleDto moimScheduleDto = new MoimScheduleResponse.MoimScheduleDto(moimSchedule.getName(), moimSchedule.getPeriod().getStartDate(), moimSchedule.getPeriod().getEndDate(),
                    moimSchedule.getPeriod().getDayInterval(), moimSchedule.getMoim().getId(), moimSchedule.getId(),
                    moimSchedule.getLocation().getX(), moimSchedule.getLocation().getY(), moimSchedule.getLocation().getLocationName());
            moimScheduleDto.setUsers(moimScheduleUserDtos, moimSchedule.getMoim().getId() == moimId, moimSchedule.getMoimMemo() != null);
            results.add(moimScheduleDto);
        }
        return results;
    }

    private List<MoimScheduleResponse.MoimScheduleDto> findIndivisualSchedule(Long moimId, LocalDateTime startDate, LocalDateTime endDate, List<User> users) {

		return queryFactory
                .select(moimSchedule).distinct()
                .from(moimSchedule)
                .join(moimAndUser.moim, moimSchedule.moim).fetchJoin()
                .join(moimAndUser.user, user).fetchJoin()
                .leftJoin(moimSchedule.moimMemo).fetchJoin()
                .where(moimSchedule.moim.id.eq(moimId)
                        .and(moimSchedule.period.startDate.loe(endDate))
                        .and(moimSchedule.period.endDate.goe(startDate))
                        .and(moimAndUser.user.in(users))
                )
                .fetch()
                .stream()
                .map(MoimScheduleResponseConverter::toMoimScheduleDto)
                .toList();
    }

    private Map<MoimSchedule, List<MoimScheduleResponse.MoimScheduleUserDto>> findMoimScheduleAndScheduleUserMap(LocalDateTime startDate, LocalDateTime endDate, List<User> users, Map<Long, MoimScheduleResponse.MoimScheduleUserDto> moimAndUserDtoMap) {
        List<MoimScheduleAndUser> moimScheduleAndUsers = queryFactory.select(moimScheduleAndUser)
                .from(moimScheduleAndUser)
                .join(moimScheduleAndUser.moimSchedule, moimSchedule).fetchJoin()
                .join(moimScheduleAndUser.category, category).fetchJoin()
                .leftJoin(moimSchedule.moimMemo).fetchJoin()
                .where(moimScheduleAndUser.user.in(users)
                        .and(moimScheduleAndUser.moimSchedule.period.startDate.loe(endDate))
                        .and(moimScheduleAndUser.moimSchedule.period.endDate.goe(startDate))
                        .and(moimScheduleAndUser.category.share.isTrue()))
                .fetch();
        return moimScheduleAndUsers.stream().collect(
                Collectors.groupingBy(
                        ((moimScheduleAndUser1 -> moimScheduleAndUser1.getMoimSchedule())),
                        Collectors.mapping(
                                (moimScheduleAndUser1 ->
                                        moimAndUserDtoMap.get(moimScheduleAndUser1.getUser().getId())),
                                Collectors.toList()
                        )
                )
        );
    }
}
