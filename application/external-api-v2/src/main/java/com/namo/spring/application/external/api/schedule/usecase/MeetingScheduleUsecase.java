package com.namo.spring.application.external.api.schedule.usecase;

import static com.namo.spring.application.external.api.schedule.converter.MeetingScheduleResponseConverter.*;
import static com.namo.spring.application.external.api.schedule.converter.ScheduleResponseConverter.*;
import static com.namo.spring.application.external.global.utils.MeetingParticipantValidationUtils.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.guest.service.GuestManageService;
import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleRequest;
import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
import com.namo.spring.application.external.api.schedule.service.ParticipantManageService;
import com.namo.spring.application.external.api.schedule.service.ScheduleManageService;
import com.namo.spring.application.external.api.user.service.MemberManageService;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Component
public class MeetingScheduleUsecase {
    private final ScheduleManageService scheduleManageService;
    private final ParticipantManageService participantManageService;
    private final MemberManageService memberManageService;
    private final GuestManageService guestManageService;

    @Transactional
    public Long createMeetingSchedule(MeetingScheduleRequest.PostMeetingScheduleDto dto, SecurityUserDetails memberInfo) {
        validateUniqueParticipantIds(memberInfo.getUserId(), dto.getParticipants());
        Member owner = memberManageService.getActiveMember(memberInfo.getUserId());
        Schedule schedule = scheduleManageService.createMeetingSchedule(dto, owner);
        return schedule.getId();
    }

    @Transactional(readOnly = true)
    public List<MeetingScheduleResponse.GetMeetingScheduleSummaryDto> getMeetingSchedules(SecurityUserDetails member) {
        return toGetMeetingScheduleSummaryDtos(scheduleManageService.getMeetingScheduleSummaries(member.getUserId()));
    }

    @Transactional(readOnly = true)
    public List<MeetingScheduleResponse.GetMonthlyMembersScheduleDto> getMonthlyMemberSchedules(List<Long> memberIds, LocalDate startDate, LocalDate endDate, SecurityUserDetails memberInfo) {
        validateUniqueParticipantIds(memberInfo.getUserId(), memberIds);

        List<ScheduleParticipantQuery> participantsWithSchedule = scheduleManageService.getMonthlyMembersSchedules(
                memberIds, startDate, endDate, memberInfo.getUserId());
        return toGetMonthlyMembersScheduleDtos(participantsWithSchedule,memberInfo.getUserId());
    }

    @Transactional(readOnly = true)
    public List<MeetingScheduleResponse.GetMonthlyMeetingParticipantScheduleDto> getMonthlyMeetingParticipantSchedules(
            Long scheduleId, LocalDate startDate, LocalDate endDate, SecurityUserDetails memberInfo) {
        Schedule schedule = scheduleManageService.getMeetingSchedule(scheduleId);
        List<ScheduleParticipantQuery> participantsWithSchedule = scheduleManageService.getMonthlyMeetingParticipantSchedules(
                schedule, startDate, endDate, memberInfo.getUserId(), null);
        return toGetMonthlyMeetingParticipantScheduleDtos(participantsWithSchedule, schedule, memberInfo.getUserId());
    }

    @Transactional(readOnly = true)
    public MeetingScheduleResponse.GetMeetingScheduleDto getMeetingSchedule(Long scheduleId,
            SecurityUserDetails memberInfo) {
        Schedule schedule = scheduleManageService.getMeetingSchedule(scheduleId);
        List<Participant> participants = scheduleManageService.getMeetingScheduleParticipants(schedule,
                memberInfo.getUserId(), null);
        return toGetMeetingScheduleDto(schedule, participants, memberInfo.getUserId());
    }

    @Transactional
    public void updateMeetingSchedule(MeetingScheduleRequest.PatchMeetingScheduleDto request, Long scheduleId,
                                      SecurityUserDetails memberInfo) {
        validateUniqueParticipantIds(memberInfo.getUserId(), request);
        Schedule schedule = scheduleManageService.getMeetingSchedule(scheduleId);
        scheduleManageService.updateMeetingSchedule(request, schedule, memberInfo.getUserId());
    }

    @Transactional
    public void updateMeetingScheduleProfile(MeetingScheduleRequest.PatchMeetingScheduleProfileDto request, Long scheduleId,
                                             SecurityUserDetails memberInfo){
        Schedule schedule = scheduleManageService.getMeetingSchedule(scheduleId);
        scheduleManageService.updateMeetingScheduleProfile(request, schedule, memberInfo.getUserId());
    }

    @Transactional
    public String getGuestInvitationUrl(Long scheduleId, SecurityUserDetails memberInfo) {
        Schedule schedule = scheduleManageService.getMeetingSchedule(scheduleId);
        scheduleManageService.validateAndGetScheduleOwner(schedule, memberInfo.getUserId());
        validateParticipantCount(scheduleManageService.getScheduleParticipantIds(schedule.getId()).size());
        return guestManageService.generateInvitationUrl(scheduleId);
    }

    @Transactional
    public void leaveMeetingSchedule(Long scheduleId, SecurityUserDetails memberInfo) {
        Schedule schedule = scheduleManageService.getMeetingSchedule(scheduleId);
        scheduleManageService.leaveMeetingSchedule(schedule, memberInfo.getUserId());
    }

    @Transactional(readOnly = true)
    public MeetingScheduleResponse.ScheduleSettlementDto getScheduleSettlement(Long memberId, Long scheduleId) {
        Schedule meetingSchedule = participantManageService.getParticipantWithScheduleAndMember(scheduleId, memberId)
                .getSchedule();
        return toScheduleSettlementDto(meetingSchedule);
    }
}
