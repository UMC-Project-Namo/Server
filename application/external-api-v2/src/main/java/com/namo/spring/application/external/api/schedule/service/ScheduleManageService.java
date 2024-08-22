package com.namo.spring.application.external.api.schedule.service;

import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.exception.ScheduleException;
import com.namo.spring.db.mysql.domains.schedule.service.ParticipantService;
import com.namo.spring.db.mysql.domains.schedule.service.ScheduleService;
import com.namo.spring.db.mysql.domains.schedule.type.ParticipantStatus;
import com.namo.spring.db.mysql.domains.schedule.type.Period;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.namo.spring.application.external.global.utils.SchedulePeriodValidationUtils.getValidatedPeriod;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleManageService {
    private final ScheduleMaker scheduleMaker;
    private final ScheduleService scheduleService;
    private final ParticipantManageService participantManageService;
    private final ParticipantService participantService;

    public Schedule getSchedule(Long scheduleId) {
        return scheduleService.readSchedule(scheduleId).orElseThrow(() -> new ScheduleException(ErrorStatus.NOT_FOUND_SCHEDULE_FAILURE));
    }

    public Schedule createPersonalSchedule(ScheduleRequest.PostPersonalScheduleDto dto, Member member) {
        Period period = getValidatedPeriod(dto.getPeriod().getStartDate(), dto.getPeriod().getEndDate());
        Schedule schedule = scheduleMaker.createPersonalSchedule(dto, period);
        participantManageService.createPersonalScheduleParticipant(member, schedule, dto.getCategoryId());
        return schedule;
    }

    public Schedule createMeetingSchedule(ScheduleRequest.PostMeetingScheduleDto dto, Member owner, MultipartFile image) {
        List<Member> participants = participantManageService.getValidatedParticipants(owner, dto.getParticipants());
        Period period = getValidatedPeriod(dto.getPeriod().getStartDate(), dto.getPeriod().getEndDate());
        Schedule schedule = scheduleMaker.createMeetingSchedule(dto, period, image);
        participantManageService.createMeetingScheduleParticipants(owner, schedule, participants);
        return schedule;
    }

    public List<Schedule> getMeetingScheduleItems(Member member) {
        List<Long> scheduleIds = participantService.readScheduleParticipantItemsByScheduleIds(member).stream()
                .map(Participant::getSchedule)
                .map(Schedule::getId)
                .collect(Collectors.toList());
        return scheduleService.readSchedulesById(scheduleIds);
    }

    public List<ScheduleParticipantQuery> getMonthlyParticipantSchedules(List<Long> memberIds, Period period, Schedule schedule, Member member) {
        List<Member> members = new ArrayList<>();
        if (schedule != null) {
            members = participantManageService.getMeetingScheduleParticipants(schedule.getId(), ParticipantStatus.ACTIVE)
                    .stream().map(Participant::getMember).collect(Collectors.toList());
        } else if (memberIds != null && !memberIds.isEmpty()) {
            members = participantManageService.getValidatedParticipants(member, memberIds);
            members.add(member);
        }
        List<Long> participantIds = members.stream().map(Member::getId).collect(Collectors.toList());
        return participantService.readParticipantsWithScheduleAndMember(participantIds, period.getStartDate(), period.getEndDate());
    }
}
