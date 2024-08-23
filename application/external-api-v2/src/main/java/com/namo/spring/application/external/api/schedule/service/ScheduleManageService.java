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
import com.namo.spring.db.mysql.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.namo.spring.application.external.global.utils.MeetingParticipantValidationUtils.validateParticipantCount;
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
        validateParticipantCount(dto.getParticipants().size());
        List<Member> participants = participantManageService.getFriendshipValidatedParticipants(owner, dto.getParticipants());
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

    public List<ScheduleParticipantQuery> getMonthlyMembersSchedules(List<Long> memberIds, Period period, Member member) {
        validateParticipantCount(memberIds.size());
        List<Long> members = participantManageService.getFriendshipValidatedParticipants(member, memberIds).stream().map(Member::getId).collect(Collectors.toList());
        members.add(member.getId());
        return participantService.readParticipantsWithScheduleAndMember(members, period.getStartDate(), period.getEndDate());
    }

    public List<ScheduleParticipantQuery> getMonthlyMeetingParticipantSchedules(Schedule schedule, Period period, Member member) {
        List<ScheduleParticipantQuery> results;
        List<Long> members;
        List<Long> anonymous;
        List<Participant> activeParticipants = participantManageService.getMeetingScheduleParticipants(schedule.getId(), ParticipantStatus.ACTIVE);

        members = activeParticipants.stream().map(Participant::getMember).filter(Objects::nonNull).map(User::getId).collect(Collectors.toList());
        anonymous = activeParticipants.stream().map(Participant::getAnonymous).filter(Objects::nonNull).map(User::getId).collect(Collectors.toList());

        results = participantService.readParticipantsWithScheduleAndMember(members, period.getStartDate(), period.getEndDate());
        results.addAll(participantService.readParticipantsWithScheduleAndAnonymous(anonymous, period.getStartDate(), period.getEndDate()));

        return results;
    }
}
