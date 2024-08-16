package com.namo.spring.application.external.api.schedule.usecase;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
import com.namo.spring.application.external.api.schedule.service.ParticipantManageService;
import com.namo.spring.application.external.api.schedule.service.ScheduleManageService;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.namo.spring.application.external.api.schedule.converter.MeetingScheduleResponseConverter.toGetMeetingScheduleItemDtos2;

@Service
@RequiredArgsConstructor
@Component
public class MeetingScheduleUsecase {
    private final ScheduleManageService scheduleManageService;
    private final ParticipantManageService participantManageService;

    public List<MeetingScheduleResponse.GetMeetingScheduleItemDto> getMeetingSchedules(Long memberId) {
        return toGetMeetingScheduleItemDtos2(scheduleManageService.getMeetingScheduleItemsByMember(memberId));
    }

    @Transactional
    public Long createMeetingSchedule(ScheduleRequest.PostMeetingScheduleDto dto, MultipartFile image, Long memberId) {
        Schedule schedule = scheduleManageService.createMeetingSchedule(dto, image, memberId);
        participantManageService.createMeetingScheduleParcitipants(memberId, schedule, dto.getParticipants());
        return schedule.getId();
    }
}
