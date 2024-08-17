package com.namo.spring.application.external.api.schedule.usecase;

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

@Service
@RequiredArgsConstructor
@Component
public class PersonalScheduleUsecase {
    private final MemberManageService memberManageService;
    private final ScheduleManageService scheduleManageService;
    private final ParticipantManageService participantManageService;

    @Transactional
    public Long createPersonalSchedule(ScheduleRequest.PostPersonalScheduleDto dto, Long memberId) {
        Member member = memberManageService.getMember(memberId);
        Schedule schedule = scheduleManageService.createPersonalSchedule(dto);
        participantManageService.createPersonalScheduleParticipant(member, schedule, dto.getCategoryId());
        return schedule.getId();
    }
}
