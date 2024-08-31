package com.namo.spring.application.external.api.schedule.usecase;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
import com.namo.spring.application.external.api.schedule.service.ScheduleManageService;
import com.namo.spring.application.external.api.user.service.MemberManageService;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.namo.spring.application.external.api.schedule.converter.MeetingScheduleResponseConverter.*;
import static com.namo.spring.application.external.global.utils.MeetingParticipantValidationUtils.validateUniqueParticipantIds;
import static com.namo.spring.application.external.global.utils.SchedulePeriodValidationUtils.getExtendedPeriod;
import static com.namo.spring.application.external.global.utils.SchedulePeriodValidationUtils.validateYearMonth;

@Slf4j
@Service
@RequiredArgsConstructor
@Component
public class MeetingScheduleUsecase {
    private final ScheduleManageService scheduleManageService;
    private final MemberManageService memberManageService;

    @Transactional
    public Long createMeetingSchedule(ScheduleRequest.PostMeetingScheduleDto dto, MultipartFile image, SecurityUserDetails memberInfo) {
        validateUniqueParticipantIds(memberInfo.getUserId(), dto.getParticipants());
        Member owner = memberManageService.getMember(memberInfo.getUserId());
        Schedule schedule = scheduleManageService.createMeetingSchedule(dto, owner, image);
        return schedule.getId();
    }

    @Transactional(readOnly = true)
    public List<MeetingScheduleResponse.GetMeetingScheduleItemDto> getMeetingSchedules(SecurityUserDetails member) {
        return toGetMeetingScheduleItemDtos(scheduleManageService.getMeetingScheduleItems(member.getUserId()));
    }

    @Transactional(readOnly = true)
    public List<MeetingScheduleResponse.GetMonthlyMembersScheduleDto> getMonthlyMemberSchedules(List<Long> memberIds, int year, int month, SecurityUserDetails memberInfo) {
        validateYearMonth(year, month);
        validateUniqueParticipantIds(memberInfo.getUserId(), memberIds);

        List<ScheduleParticipantQuery> participantsWithSchedule = scheduleManageService.getMonthlyMembersSchedules(memberIds, getExtendedPeriod(year, month), memberInfo.getUserId());
        return toGetMonthlyParticipantScheduleDtos(participantsWithSchedule, memberIds, memberInfo.getUserId());
    }

    @Transactional(readOnly = true)
    public List<MeetingScheduleResponse.GetMonthlyMeetingParticipantScheduleDto> getMonthlyMeetingParticipantSchedules(Long scheduleId, int year, int month, SecurityUserDetails memberInfo) {
        validateYearMonth(year, month);

        Schedule schedule = scheduleManageService.getMeetingSchedule(scheduleId);
        List<ScheduleParticipantQuery> participantsWithSchedule = scheduleManageService.getMonthlyMeetingParticipantSchedules(schedule, getExtendedPeriod(year, month), memberInfo.getUserId());
        return toGetMonthlyMeetingParticipantScheduleDtos(participantsWithSchedule, schedule);
    }

    @Transactional(readOnly = true)
    public MeetingScheduleResponse.GetMeetingScheduleDto getMeetingSchedule(Long scheduleId, SecurityUserDetails memberInfo) {
        Schedule schedule = scheduleManageService.getMeetingSchedule(scheduleId);
        List<Participant> participants = scheduleManageService.getMeetingScheduleParticipants(schedule, memberInfo.getUserId());
        return toGetMeetingScheduleDto(schedule, participants);
    }

    @Transactional
    public void updateMeetingSchedule(ScheduleRequest.PatchMeetingScheduleDto dto, Long scheduleId, SecurityUserDetails memberInfo) {
        validateUniqueParticipantIds(memberInfo.getUserId(), dto);
        Schedule schedule = scheduleManageService.getMeetingSchedule(scheduleId);
        scheduleManageService.updateMeetingSchedule(dto, schedule, memberInfo.getUserId());
    }
}
