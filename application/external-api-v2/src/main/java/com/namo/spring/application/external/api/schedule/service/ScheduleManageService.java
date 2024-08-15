package com.namo.spring.application.external.api.schedule.service;

import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.category.service.CategoryService;
import com.namo.spring.db.mysql.domains.schedule.dto.MeetingScheduleQueryDto;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.exception.ScheduleException;
import com.namo.spring.db.mysql.domains.schedule.service.ScheduleService;
import com.namo.spring.db.mysql.domains.schedule.type.Period;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;
import com.namo.spring.db.mysql.domains.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleManageService {
    private final ScheduleMaker scheduleMaker;
    private final ScheduleService scheduleService;
    private final MemberService memberService;
    private final CategoryService categoryService;

    @Transactional(readOnly = true)
    public Member getMember(Long memberId) {
        return memberService.readMember(memberId).orElseThrow(
                () -> new MemberException(ErrorStatus.NOT_FOUND_USER_FAILURE)
        );
    }

    @Transactional(readOnly = true)
    public List<MeetingScheduleQueryDto> getMeetingSchedulesByMember(Long memberId) {
        Member member = getMember(memberId);
        return scheduleService.readMeetingSchedulesWithParticipantsByMember(member);
    }

    @Transactional
    public Schedule createPersonalSchedule(ScheduleRequest.PostPersonalScheduleDto dto, Long memberId) {
        Period period = getValidatedPeriod(dto.getStartDate(), dto.getEndDate());
        Schedule schedule = scheduleMaker.createPersonalSchedule(dto, period);
        return schedule;
    }

    @Transactional
    public Schedule createMeetingSchedule(ScheduleRequest.PostMeetingScheduleDto dto, MultipartFile image, Long memberId) {
        Period period = getValidatedPeriod(dto.getStartDate(), dto.getEndDate());
        Schedule schedule = scheduleMaker.createMeetingSchedule(dto, period, image);
        return schedule;
    }

    private Period getValidatedPeriod(Long startDate, Long endDate) {
        Period period = Period.of(startDate, endDate);
        if (period.getStartDate().isAfter(period.getEndDate())) {
            throw new ScheduleException(ErrorStatus.INVALID_DATE);
        }
        return period;
    }
}
