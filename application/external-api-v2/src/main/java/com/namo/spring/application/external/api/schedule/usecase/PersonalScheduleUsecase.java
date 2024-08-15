package com.namo.spring.application.external.api.schedule.usecase;

import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
import com.namo.spring.application.external.api.schedule.service.ParticipantManageService;
import com.namo.spring.application.external.api.schedule.service.ScheduleManageService;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Component
public class PersonalScheduleUsecase {
    private final ScheduleManageService scheduleManageService;
    private final ParticipantManageService participantManageService;

    @Transactional
    public Long createPersonalSchedule(ScheduleRequest.PostPersonalScheduleDto dto, Long memberId) {
        Schedule schedule = scheduleManageService.createPersonalSchedule(dto, memberId);
        participantManageService.createPersonalScheduleParticipant(memberId, schedule, dto.getCategoryId());
        return schedule.getId();
    }
}
