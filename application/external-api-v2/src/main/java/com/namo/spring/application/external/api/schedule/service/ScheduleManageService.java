package com.namo.spring.application.external.api.schedule.service;

import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.exception.ScheduleException;
import com.namo.spring.db.mysql.domains.schedule.repository.ParticipantRepository;
import com.namo.spring.db.mysql.domains.schedule.service.ParticipantService;
import com.namo.spring.db.mysql.domains.schedule.service.ScheduleService;
import com.namo.spring.db.mysql.domains.schedule.type.*;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.namo.spring.application.external.api.schedule.converter.ScheduleConverter.toLocation;
import static com.namo.spring.application.external.global.utils.MeetingParticipantValidationUtils.validateExistingAndNewParticipantIds;
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

    private final ParticipantRepository participantRepository;

    public Schedule getMeetingSchedule(Long scheduleId) {
        Schedule schedule = scheduleService.readSchedule(scheduleId).orElseThrow(() -> new ScheduleException(ErrorStatus.NOT_FOUND_SCHEDULE_FAILURE));
        if (schedule.getScheduleType() != ScheduleType.MEETING.getValue()) {
            throw new ScheduleException(ErrorStatus.NOT_MEETING_SCHEDULE);
        }
        return schedule;
    }

    public Schedule createPersonalSchedule(ScheduleRequest.PostPersonalScheduleDto dto, Member member) {
        Period period = getValidatedPeriod(dto.getPeriod().getStartDate(), dto.getPeriod().getEndDate());
        Schedule schedule = scheduleMaker.createPersonalSchedule(dto, period);
        participantManageService.createPersonalScheduleParticipant(member, schedule, dto.getCategoryId());
        return schedule;
    }

    public Schedule createMeetingSchedule(ScheduleRequest.PostMeetingScheduleDto dto, Member owner, MultipartFile image) {
        validateParticipantCount(dto.getParticipants().size());
        List<Member> participants = participantManageService.getFriendshipValidatedParticipants(owner.getId(), dto.getParticipants());
        Period period = getValidatedPeriod(dto.getPeriod().getStartDate(), dto.getPeriod().getEndDate());
        Schedule schedule = scheduleMaker.createMeetingSchedule(dto, period, image);
        participantManageService.createMeetingScheduleParticipants(owner, schedule, participants);
        return schedule;
    }

    public List<Schedule> getMeetingScheduleItems(Long memberId) {
        List<Long> scheduleIds = participantService.readScheduleParticipantItemsByScheduleIds(memberId).stream()
                .map(Participant::getSchedule)
                .map(Schedule::getId)
                .collect(Collectors.toList());
        return scheduleService.readSchedulesById(scheduleIds);
    }

    public List<ScheduleParticipantQuery> getMonthlyMembersSchedules(List<Long> memberIds, Period period, Long memberId) {
        validateParticipantCount(memberIds.size());
        List<Long> members = participantManageService.getFriendshipValidatedParticipants(memberId, memberIds).stream().map(Member::getId).collect(Collectors.toList());
        members.add(memberId);
        return participantService.readParticipantsWithScheduleAndAnonymous(members, period.getStartDate(), period.getEndDate());
    }

    public List<ScheduleParticipantQuery> getMonthlyMeetingParticipantSchedules(Schedule schedule, Period period, Long memberId) {
        checkParticipantExists(schedule, memberId);
        List<Participant> participants = participantManageService.getMeetingScheduleParticipants(schedule.getId(), ParticipantStatus.ACTIVE);
        List<Long> members = participants.stream().map(Participant::getUser).map(User::getId).collect(Collectors.toList());

        return participantService.readParticipantsWithScheduleAndAnonymous(members, period.getStartDate(), period.getEndDate());
    }

    private void checkParticipantExists(Schedule schedule, Long memberId) {
        if (!participantService.existsByScheduleIdAndMemberId(schedule.getId(), memberId)) {
            throw new ScheduleException(ErrorStatus.NOT_SCHEDULE_PARTICIPANT);
        }
    }

    public List<Participant> getMeetingScheduleParticipants(Schedule schedule, Long memberId) {
        checkParticipantExists(schedule, memberId);
        return participantService.readParticipantsByScheduleIdAndStatusAndType(schedule.getId(), ScheduleType.MEETING, null);
    }

    public void updateMeetingSchedule(ScheduleRequest.PatchMeetingScheduleDto dto, Schedule schedule, Long memberId) {
        Participant ownerParticipant = participantManageService.getValidatedMeetingParticipantWithSchedule(memberId, schedule.getId());
        checkParticipantIsOwner(ownerParticipant);
        // 기존의 인원수와, 초대될 + 삭제될 member의 인원 수 검증
        List<Long> participantIds = participantRepository.findAllByScheduleId(schedule.getId()).stream()
                .filter(participant -> participant.getIsOwner() == ParticipantRole.NON_OWNER.getValue())
                .map(Participant::getMember)
                .map(Member::getId)
                .collect(Collectors.toList());
        updateScheduleContent(dto.getTitle(), dto.getLocation(), dto.getPeriod(), schedule);
        if (!dto.getParticipantsToAdd().isEmpty() || !dto.getParticipantsToRemove().isEmpty()) {
            validateParticipantCount(participantIds.size() + dto.getParticipantsToAdd().size() - dto.getParticipantsToRemove().size());
            validateExistingAndNewParticipantIds(participantIds, dto.getParticipantsToAdd());
            participantManageService.updateMeetingScheduleParticipants(memberId, schedule, dto);
        }
    }

    private void updateScheduleContent(String title, ScheduleRequest.LocationDto locationDto, ScheduleRequest.PeriodDto periodDto, Schedule schedule) {
        Period period = getValidatedPeriod(periodDto.getStartDate(), periodDto.getEndDate());
        Location location = locationDto != null ? toLocation(locationDto) : null;
        schedule.updateContent(title, period, location);
    }

    private void checkParticipantIsOwner(Participant participant) {
        if (participant.getIsOwner() != ParticipantRole.OWNER.getValue()) {
            throw new ScheduleException(ErrorStatus.NOT_SCHEDULE_OWNER);
        }
    }
}
