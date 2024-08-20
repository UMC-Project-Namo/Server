package com.namo.spring.application.external.api.schedule.service;

import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
import com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.service.ParticipantService;
import com.namo.spring.db.mysql.domains.schedule.service.ScheduleService;
import com.namo.spring.db.mysql.domains.schedule.type.Period;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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

    public Schedule createPersonalSchedule(ScheduleRequest.PostPersonalScheduleDto dto, Member member) {
        Period period = getValidatedPeriod(dto.getPeriod().getStartDate(), dto.getPeriod().getEndDate());
        Schedule schedule = scheduleMaker.createPersonalSchedule(dto, period);
        participantManageService.createPersonalScheduleParticipant(member, schedule, dto.getCategoryId());
        return schedule;
    }

    public Schedule createMeetingSchedule(ScheduleRequest.PostMeetingScheduleDto dto, Member owner, MultipartFile image) {
        List<Member> participants = participantManageService.getValidatedMeetingParticipants(owner, dto.getParticipants());
        Period period = getValidatedPeriod(dto.getPeriod().getStartDate(), dto.getPeriod().getEndDate());
        Schedule schedule = scheduleMaker.createMeetingSchedule(dto, period, image);
        participantManageService.createMeetingScheduleParticipants(owner, schedule, participants);
        return schedule;
    }

    public List<Schedule> getMeetingScheduleItems(Member member) {
        List<Long> scheduleIds = participantService.findScheduleParticipantItemsByScheduleIds(member).stream()
                .map(Participant::getSchedule)
                .map(Schedule::getId)
                .collect(Collectors.toList());
        return scheduleService.readSchedulesById(scheduleIds);
    }

    public List<ScheduleParticipantQuery> getMeetingParticipantSchedules(List<Long> memberIds, List<LocalDateTime> period, Long scheduleId, Member member) {
        List<Member> members = new ArrayList<>();
        if (scheduleId != null) {
        } else {
            members = participantManageService.getValidatedMeetingParticipants(member, memberIds);
            members.add(member);
        }
        return participantManageService.getMeetingParticipantWithSchedule(members, period);
    }
}
