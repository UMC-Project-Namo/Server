package com.namo.spring.application.external.api.guest.usecase;

import static com.namo.spring.application.external.api.guest.converter.GuestMeetingResponseConverter.*;
import static com.namo.spring.application.external.api.guest.converter.GuestParticipantResponseConverter.*;
import static com.namo.spring.application.external.global.utils.SchedulePeriodValidationUtils.*;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.guest.dto.GuestMeetingResponse;
import com.namo.spring.application.external.api.guest.dto.GuestParticipantRequest;
import com.namo.spring.application.external.api.guest.dto.GuestParticipantResponse;
import com.namo.spring.application.external.api.guest.service.GuestManageService;
import com.namo.spring.application.external.api.schedule.service.ScheduleManageService;
import com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.user.entity.Anonymous;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Component
public class GuestUsecase {
    private final ScheduleManageService scheduleManageService;
    private final GuestManageService guestManageService;

    @Transactional
    public GuestParticipantResponse.PostGuestParticipantDto createOrValidateGuest(
            GuestParticipantRequest.PostGuestParticipantDto dto, String code) {
        Long scheduleId = guestManageService.decodeInviteCode(code);
        Schedule schedule = scheduleManageService.getMeetingSchedule(scheduleId);
        return toPostGuestParticipantDto(guestManageService.createOrValidateGuest(dto, schedule, code));
    }

    @Transactional(readOnly = true)
    public List<GuestMeetingResponse.GetMonthlyMeetingParticipantScheduleDto> getMonthlyMeetingParticipantSchedules(
            Long scheduleId, int year, int month, String tag, String nickname) {
        validateYearMonth(year, month);
        Anonymous anonymous = guestManageService.getAnonymousByTagAndNickname(tag, nickname);
        Schedule schedule = scheduleManageService.getMeetingSchedule(scheduleId);
        List<ScheduleParticipantQuery> participantsWithSchedule = scheduleManageService.getMonthlyMeetingParticipantSchedules(
                schedule, getExtendedPeriod(year, month), null, anonymous.getId());
        return toGetMonthlyMeetingParticipantScheduleDtos(participantsWithSchedule, schedule, null);
    }
}
