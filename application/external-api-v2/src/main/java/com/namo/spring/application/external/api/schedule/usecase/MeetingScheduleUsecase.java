package com.namo.spring.application.external.api.schedule.usecase;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
import com.namo.spring.application.external.api.schedule.service.ScheduleManageService;
import com.namo.spring.application.external.api.user.service.MemberManageService;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.namo.spring.application.external.api.schedule.converter.MeetingScheduleResponseConverter.toGetMeetingScheduleItemDtos;
import static com.namo.spring.application.external.global.utils.MeetingValidationUtils.validateOwnerNotInParticipants;
import static com.namo.spring.application.external.global.utils.MeetingValidationUtils.validateParticipantCount;

@Service
@RequiredArgsConstructor
@Component
public class MeetingScheduleUsecase {
    private final ScheduleManageService scheduleManageService;
    private final MemberManageService memberManageService;

    @Transactional(readOnly = true)
    public List<MeetingScheduleResponse.GetMeetingScheduleItemDto> getMeetingSchedules(Long memberId) {
        Member member = memberManageService.getMember(memberId);
        return toGetMeetingScheduleItemDtos(scheduleManageService.getMeetingScheduleItemsByMember(member));
    }

    @Transactional
    public Long createMeetingSchedule(ScheduleRequest.PostMeetingScheduleDto dto, MultipartFile image, Long memberId) {
        validateOwnerNotInParticipants(memberId, dto.getParticipants());
        validateParticipantCount(dto.getParticipants().size());
        Member owner = memberManageService.getMember(memberId);
        Schedule schedule = scheduleManageService.createMeetingSchedule(dto, owner, image);
        return schedule.getId();
    }
}
