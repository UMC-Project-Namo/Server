package com.namo.spring.application.external.api.schedule.usecase;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
import com.namo.spring.application.external.api.schedule.service.ParticipantManageService;
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

@Service
@RequiredArgsConstructor
@Component
public class MeetingScheduleUsecase {
    private final ScheduleManageService scheduleManageService;
    private final MemberManageService memberManageService;
    private final ParticipantManageService participantManageService;

    @Transactional(readOnly = true)
    public List<MeetingScheduleResponse.GetMeetingScheduleItemDto> getMeetingSchedules(Long memberId) {
        Member member = memberManageService.getMember(memberId);
        return toGetMeetingScheduleItemDtos(scheduleManageService.getMeetingScheduleItemsByMember(member));
    }

    @Transactional
    public Long createMeetingSchedule(ScheduleRequest.PostMeetingScheduleDto dto, MultipartFile image, Long memberId) {
        Member scheduleOwner = memberManageService.getMember(memberId);
        List<Member> participants = participantManageService.getValidatedMeetingParticipants(dto.getParticipants());
        Schedule schedule = scheduleManageService.createMeetingSchedule(dto, image);
        participantManageService.createMeetingScheduleParticipants(scheduleOwner, schedule, participants);
        return schedule.getId();
    }
}
