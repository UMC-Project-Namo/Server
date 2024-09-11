package com.namo.spring.application.external.api.schedule.usecase;

import com.namo.spring.application.external.api.schedule.dto.GuestParticipantRequest;
import com.namo.spring.application.external.api.schedule.dto.GuestParticipantResponse;
import com.namo.spring.application.external.api.schedule.service.GuestManageService;
import com.namo.spring.application.external.api.schedule.service.ScheduleManageService;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.namo.spring.application.external.api.schedule.converter.GuestParticipantResponseConverter.toPostGuestParticipantDto;

@Slf4j
@Service
@RequiredArgsConstructor
@Component
public class GuestMeetingUsecase {
    private final ScheduleManageService scheduleManageService;
    private final GuestManageService guestManageService;

    @Transactional
    public GuestParticipantResponse.PostGuestParticipantDto createOrValidateGuestParticipant(GuestParticipantRequest.PostGuestParticipantDto dto, String code) {
        Long scheduleId = guestManageService.decodeInviteCode(code);
        Schedule schedule = scheduleManageService.getMeetingSchedule(scheduleId);
        return toPostGuestParticipantDto(guestManageService.createOrValidateGuest(dto, schedule, code));
    }

}
