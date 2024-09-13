package com.namo.spring.application.external.api.schedule.service;

import static com.namo.spring.application.external.global.utils.MeetingParticipantValidationUtils.*;
import static com.namo.spring.application.external.global.utils.SchedulePeriodValidationUtils.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.namo.spring.application.external.api.schedule.converter.LocationConverter;
import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleRequest;
import com.namo.spring.application.external.api.schedule.dto.PersonalScheduleRequest;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.exception.ScheduleException;
import com.namo.spring.db.mysql.domains.schedule.service.ParticipantService;
import com.namo.spring.db.mysql.domains.schedule.service.ScheduleService;
import com.namo.spring.db.mysql.domains.schedule.type.Location;
import com.namo.spring.db.mysql.domains.schedule.type.ParticipantRole;
import com.namo.spring.db.mysql.domains.schedule.type.ParticipantStatus;
import com.namo.spring.db.mysql.domains.schedule.type.Period;
import com.namo.spring.db.mysql.domains.schedule.type.ScheduleType;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.entity.User;
import com.namo.spring.db.mysql.domains.user.service.FriendshipService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleManageService {
    private final ScheduleMaker scheduleMaker;
    private final ScheduleService scheduleService;
    private final ParticipantManageService participantManageService;
    private final ParticipantService participantService;
    private final FriendshipService friendshipService;

    public Schedule getSchedule(Long scheduleId) {
        return scheduleService.readSchedule(scheduleId)
                .orElseThrow(() -> new ScheduleException(ErrorStatus.NOT_FOUND_SCHEDULE_FAILURE));
    }

    public Schedule getPersonalSchedule(Long scheduleId) {
        Schedule schedule = getSchedule(scheduleId);
        if (schedule.getScheduleType() != ScheduleType.PERSONAL.getValue()) {
            throw new ScheduleException(ErrorStatus.NOT_PERSONAL_SCHEDULE);
        }
        return schedule;
    }

    public Schedule getMeetingSchedule(Long scheduleId) {
        Schedule schedule = scheduleService.readSchedule(scheduleId)
                .orElseThrow(() -> new ScheduleException(ErrorStatus.NOT_FOUND_SCHEDULE_FAILURE));
        if (schedule.getScheduleType() != ScheduleType.MEETING.getValue()) {
            throw new ScheduleException(ErrorStatus.NOT_MEETING_SCHEDULE);
        }
        return schedule;
    }

    public Schedule createPersonalSchedule(PersonalScheduleRequest.PostPersonalScheduleDto dto, Member member) {
        Period period = getValidatedPeriod(dto.getPeriod().getStartDate(), dto.getPeriod().getEndDate());
        Schedule schedule = scheduleMaker.createPersonalSchedule(dto, period, member.getNickname());
        participantManageService.createPersonalScheduleParticipant(member, schedule, dto.getCategoryId());
        return schedule;
    }

    public Schedule createMeetingSchedule(MeetingScheduleRequest.PostMeetingScheduleDto dto, Member owner) {
        validateParticipantCount(dto.getParticipants().size());
        List<Member> participants = participantManageService.getFriendshipValidatedParticipants(owner.getId(),
                dto.getParticipants());
        Period period = getValidatedPeriod(dto.getPeriod().getStartDate(), dto.getPeriod().getEndDate());
        Schedule schedule = scheduleMaker.createMeetingSchedule(dto, period);
        participantManageService.createMeetingScheduleParticipants(owner, schedule, participants);
        return schedule;
    }

    public List<Schedule> getMeetingScheduleItems(Long memberId) {
        List<Long> scheduleIds = participantService.readScheduleParticipantSummaryByScheduleIds(memberId).stream()
                .map(Participant::getSchedule)
                .map(Schedule::getId)
                .collect(Collectors.toList());
        return scheduleService.readSchedulesById(scheduleIds);
    }

    public List<Participant> getMyMonthlySchedules(Long memberId, Period period) {
        return participantService.readParticipantsWithScheduleAndCategoryByPeriod(memberId, null, period.getStartDate(),
                period.getEndDate());
    }

    public List<Participant> getMemberMonthlySchedules(Long targetMemberId, Long friendId, Period period) {
        checkMemberIsFriend(targetMemberId, friendId);
        return participantService.readParticipantsWithScheduleAndCategoryByPeriod(targetMemberId, Boolean.TRUE,
                        period.getStartDate(), period.getEndDate()).stream()
                .filter(participant -> participant.getCategory().isShared())
                .collect(Collectors.toList());
    }

    private void checkMemberIsFriend(Long memberId, Long friendId) {
        if (!friendshipService.existsByMemberIdAndFriendId(memberId, friendId)) {
            throw new ScheduleException(ErrorStatus.NOT_FRIENDSHIP_MEMBER);
        }
    }

    public List<ScheduleParticipantQuery> getMonthlyMembersSchedules(List<Long> memberIds, Period period,
            Long memberId) {
        validateParticipantCount(memberIds.size());
        List<Long> members = participantManageService.getFriendshipValidatedParticipants(memberId, memberIds)
                .stream()
                .map(Member::getId)
                .collect(Collectors.toList());
        members.add(memberId);
        return participantService.readParticipantsWithUserAndScheduleByPeriod(members, period.getStartDate(),
                        period.getEndDate())
                .stream()
                // 다른 유저의 일정일 경우 공유 여부로 필터링
                .filter(participant -> {
                    boolean isSharedSchedule = true;
                    if (!participant.getMemberId().equals(memberId))
                        isSharedSchedule = participant.getIsShared();
                    return isSharedSchedule;
                })
                .collect(Collectors.toList());
    }

    public List<ScheduleParticipantQuery> getMonthlyMeetingParticipantSchedules(Schedule schedule, Period period,
            Long memberId, Long anonymousId) {
        checkUserIsParticipant(schedule, memberId, null);
        List<Participant> participants = participantService.readParticipantsByScheduleIdAndStatusAndType(
                schedule.getId(), ScheduleType.MEETING, ParticipantStatus.ACTIVE);
        List<Long> members = participants.stream()
                .map(Participant::getUser)
                .map(User::getId)
                .collect(Collectors.toList());

        return participantService.readParticipantsWithUserAndScheduleByPeriod(members, period.getStartDate(),
                        period.getEndDate())
                .stream()
                // 다른 유저의 일정일 경우 공유 여부로 필터링
                .filter(participant -> isSharedOrOwnSchedule(participant, memberId, anonymousId))
                .collect(Collectors.toList());
    }

    /**
     * 게스트 유저가 조회할 경우 공유된 일정만을,
     * 회원 유저가 조회할 경우 조회하는 회원의 비공개 일정까지 조회되도록
     * 필터링 합니다.
     *
     * @param participant
     * @param memberId
     * @param anonymousId
     * @return 조회하는 회원의 비공개 일정 조회 여부
     */
    private boolean isSharedOrOwnSchedule(ScheduleParticipantQuery participant, Long memberId, Long anonymousId) {
        if (anonymousId != null) {
            return true;
        }
        // memberId가 있는 경우
        return participant.getMemberId().equals(memberId) || participant.getIsShared();
    }

    private void checkUserIsParticipant(Schedule schedule, Long memberId, Long anonymousId) {
        boolean isParticipant;
        if (memberId != null)
            isParticipant = participantService.existsByScheduleIdAndMemberId(schedule.getId(), memberId);
        else
            isParticipant = participantService.existsByScheduleIdAndAnonymousId(schedule.getId(), anonymousId);
        if (!isParticipant) {
            throw new ScheduleException(ErrorStatus.NOT_SCHEDULE_PARTICIPANT);
        }
    }

    public List<Participant> getMeetingScheduleParticipants(Schedule schedule, Long memberId, Long anonymousId) {
        checkUserIsParticipant(schedule, memberId, anonymousId);
        return participantService.readParticipantsByScheduleIdAndStatusAndType(schedule.getId(), ScheduleType.MEETING,
                null);
    }

    public void updatePersonalSchedule(PersonalScheduleRequest.PatchPersonalScheduleDto dto, Schedule schedule,
            Long memberId) {
        validateScheduleOwner(schedule, memberId);
        updateScheduleContent(dto.getTitle(), dto.getLocation(), dto.getPeriod(), null, schedule);
    }

    /**
     * 모임 일정의 정보를 수정합니다.
     */
    public void updateMeetingSchedule(MeetingScheduleRequest.PatchMeetingScheduleDto dto, Schedule schedule,
                                      Long memberId) {
        validateScheduleOwner(schedule, memberId);
        updateScheduleContent(dto.getTitle(), dto.getLocation(), dto.getPeriod(), dto.getImageUrl(), schedule);
        // 기존의 인원과, 초대될 & 삭제될 member  검증
        if (!dto.getParticipantsToAdd().isEmpty() || !dto.getParticipantsToRemove().isEmpty()) {
            List<Long> participantIds = getScheduleParticipantIds(schedule.getId());
            validateParticipantCount(
                    participantIds.size() + dto.getParticipantsToAdd().size() - dto.getParticipantsToRemove().size());
            validateExistingAndNewParticipantIds(participantIds, dto.getParticipantsToAdd());
            participantManageService.updateMeetingScheduleParticipants(memberId, schedule, dto);
        }
    }

    /**
     * 모임 일정의 제목, 이미지를 커스텀 합니다.
     */
    public void updateMeetingScheduleProfile(MeetingScheduleRequest.PatchMeetingScheduleProfileDto dto, Schedule schedule,
                                             Long memberId){
        Participant participant = participantManageService.getScheduleParticipant(memberId, schedule.getId());
        participant.updateCustomScheduleInfo(dto.getTitle(), dto.getImageUrl());
    }

    /**
     * 모임 일정에 대한 모든 참여자의 ID를 반환합니다,
     * 반환 값에는 활성, 비활성 모든 상태의 참여자가 포함됩니다.
     * 모임 일정 초대 인원 수를 검증하기 위해 사용합니다.
     *
     * @param scheduleId
     * @return 모임 일정에 대한 모든 참여자의 ID 배열
     */
    public List<Long> getScheduleParticipantIds(Long scheduleId) {
        List<Long> participantIds = participantService.readParticipantsByScheduleId(scheduleId).stream()
                .filter(participant -> participant.getIsOwner() == ParticipantRole.NON_OWNER.getValue())
                .map(Participant::getMember)
                .map(Member::getId)
                .collect(Collectors.toList());
        return participantIds;
    }

    private void updateScheduleContent(String title, MeetingScheduleRequest.LocationDto locationDto,
            MeetingScheduleRequest.PeriodDto periodDto, String imageUrl, Schedule schedule) {
        Period period = getValidatedPeriod(periodDto.getStartDate(), periodDto.getEndDate());
        Location location = locationDto != null ? LocationConverter.toLocation(locationDto) : null;
        schedule.updateContent(title, period, location, imageUrl);
    }

    public void validateScheduleOwner(Schedule schedule, Long memberId) {
        Participant participant = participantManageService.getValidatedParticipantWithSchedule(memberId,
                schedule.getId());
        if (participant.getIsOwner() != ParticipantRole.OWNER.getValue()) {
            throw new ScheduleException(ErrorStatus.NOT_SCHEDULE_OWNER);
        }
    }

}
