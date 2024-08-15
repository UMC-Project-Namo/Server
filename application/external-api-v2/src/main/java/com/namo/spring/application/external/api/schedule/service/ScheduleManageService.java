package com.namo.spring.application.external.api.schedule.service;

import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.exception.ScheduleException;
import com.namo.spring.db.mysql.domains.schedule.service.ParticipantService;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleManageService {
    private final ScheduleMaker scheduleMaker;
    private final ScheduleService scheduleService;
    private final ParticipantService participantService;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public Member getMember(Long memberId) {
        return memberService.readMember(memberId).orElseThrow(
                () -> new MemberException(ErrorStatus.NOT_FOUND_USER_FAILURE)
        );
    }

    @Transactional(readOnly = true)
    public List<Participant> getMeetingSchedulesByMember(Long memberId) {
        Member member = getMember(memberId);
        List<Long> participantIds = participantService.findParticipantsWithSchedulesByScheduleIds(member).stream()
                .map(Participant::getSchedule)
                .map(Schedule::getId)
                .collect(Collectors.toList());
        List<Participant> participantsWithSchedules = participantService.findParticipantsWithSchedulesByScheduleIds(participantIds);
        return participantsWithSchedules;
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
