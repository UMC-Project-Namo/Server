package com.namo.spring.application.external.api.schedule.usecase;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
import com.namo.spring.application.external.api.schedule.service.ScheduleManageService;
import com.namo.spring.application.external.api.user.service.MemberManageService;
import com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.namo.spring.application.external.api.schedule.converter.MeetingScheduleResponseConverter.toGetMeetingScheduleItemDtos;
import static com.namo.spring.application.external.api.schedule.converter.MeetingScheduleResponseConverter.toGetMonthlyParticipantScheduleDtos;
import static com.namo.spring.application.external.global.utils.MeetingParticipantValidationUtils.validateParticipantCount;
import static com.namo.spring.application.external.global.utils.MeetingParticipantValidationUtils.validateParticipantIds;
import static com.namo.spring.application.external.global.utils.SchedulePeriodValidationUtils.getExtendedPeriod;
import static com.namo.spring.application.external.global.utils.SchedulePeriodValidationUtils.validateYearMonth;

@Service
@RequiredArgsConstructor
@Component
public class MeetingScheduleUsecase {
    private final ScheduleManageService scheduleManageService;
    private final MemberManageService memberManageService;

    @Transactional
    public Long createMeetingSchedule(ScheduleRequest.PostMeetingScheduleDto dto, MultipartFile image, Long memberId) {
        validateParticipantCount(dto.getParticipants().size());
        validateParticipantIds(memberId, dto.getParticipants());
        Member owner = memberManageService.getMember(memberId);
        Schedule schedule = scheduleManageService.createMeetingSchedule(dto, owner, image);
        return schedule.getId();
    }

    @Transactional(readOnly = true)
    public List<MeetingScheduleResponse.GetMeetingScheduleItemDto> getMeetingSchedules(Long memberId) {
        Member member = memberManageService.getMember(memberId);
        return toGetMeetingScheduleItemDtos(scheduleManageService.getMeetingScheduleItems(member));
    }

    public List<MeetingScheduleResponse.GetMonthlyParticipantScheduleDto> getMeetingParticiantsSchedules(List<Long> memberIds, int year, int month, Long memberId) {
        validateYearMonth(year, month);
        validateParticipantCount(memberIds.size());
        validateParticipantIds(memberId, memberIds);

        Member member = memberManageService.getMember(memberId);
        List<ScheduleParticipantQuery> participantsWithSchedule = scheduleManageService.getMeetingParticipantSchedules(memberIds, getExtendedPeriod(year, month), null, member);
        return toGetMonthlyParticipantScheduleDtos(participantsWithSchedule);
    }
}
