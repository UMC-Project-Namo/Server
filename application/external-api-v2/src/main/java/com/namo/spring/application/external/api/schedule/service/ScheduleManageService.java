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
    private final ParticipantManageService participantManageService;
    private final ParticipantService participantService;

    @Transactional(readOnly = true)
    public List<Schedule> getMeetingScheduleItemsByMember(Member member) {
        List<Long> scheduleIds = participantService.findScheduleParticipantItemsByScheduleIds(member).stream()
                .map(Participant::getSchedule)
                .map(Schedule::getId)
                .collect(Collectors.toList());
        return scheduleService.readSchedulesById(scheduleIds);
    }

    @Transactional
    public Schedule createPersonalSchedule(ScheduleRequest.PostPersonalScheduleDto dto, Member member) {
        Period period = getValidatedPeriod(dto.getPeriod());
        Schedule schedule = scheduleMaker.createPersonalSchedule(dto, period);
        participantManageService.createPersonalScheduleParticipant(member, schedule, dto.getCategoryId());
        return schedule;
    }

    @Transactional
    public Schedule createMeetingSchedule(ScheduleRequest.PostMeetingScheduleDto dto, Member scheduleOwner, MultipartFile image) {
        Period period = getValidatedPeriod(dto.getPeriod());
        Schedule schedule = scheduleMaker.createMeetingSchedule(dto, period, image);
        List<Member> participants = participantManageService.getValidatedMeetingParticipants(dto.getParticipants());
        participantManageService.createMeetingScheduleParticipants(scheduleOwner, schedule, participants);
        return schedule;
    }

    private Period getValidatedPeriod(ScheduleRequest.PeriodDto dto) {
        Period period = Period.of(dto.getStartDate(), dto.getEndDate());
        if (period.getStartDate().isAfter(period.getEndDate())) {
            throw new ScheduleException(ErrorStatus.INVALID_DATE);
        }
        return period;
    }
}
