package com.namo.spring.application.external.api.schedule.usecase;

import com.namo.spring.application.external.api.schedule.service.ParticipantManageService;
import com.namo.spring.application.external.api.user.service.MemberManageService;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.schedule.exception.ScheduleException;
import com.namo.spring.db.mysql.domains.schedule.service.ParticipantService;
import com.namo.spring.db.mysql.domains.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Component
public class ParticipantUsecase {

    private final MemberManageService memberManageService;
    private final ParticipantManageService participantManageService;
    private final ParticipantService participantService;
    private final ScheduleService scheduleService;

    private void checkMemberIsOwner(Long scheduleId, Long memberId) {
        if (!participantService.existsParticipantByMemberIdAndScheduleId(scheduleId, memberId)) {
            throw new ScheduleException(ErrorStatus.NOT_SCHEDULE_OWNER);
        }
    }
}
