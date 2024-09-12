package com.namo.spring.application.external.api.guest.controller;

import com.namo.spring.application.external.api.guest.dto.GuestMeetingResponse;
import com.namo.spring.application.external.api.guest.dto.GuestParticipantRequest;
import com.namo.spring.application.external.api.guest.dto.GuestParticipantResponse;
import com.namo.spring.application.external.api.guest.usecase.GuestMeetingUsecase;
import com.namo.spring.core.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "게스트", description = "게스트 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/guests")
@PreAuthorize("isAnonymous()")
public class GuestController {
    private final GuestMeetingUsecase guestMeetingUsecase;

    /**
     * 게스트 로그인 / 모임 가입 API
     */
    @PostMapping(path = "/invitations")
    public ResponseDto<GuestParticipantResponse.PostGuestParticipantDto> guestMeetingAccess(
            @RequestParam String code,
            @Valid @RequestBody GuestParticipantRequest.PostGuestParticipantDto dto) {
        return ResponseDto.onSuccess(guestMeetingUsecase.createOrValidateGuest(dto, code));
    }

    /**
     * 게스트 모임 일정 월간 조회 API
     */
    @GetMapping(path = "/meeting/{meetingScheduleId}")
    public ResponseDto<List<GuestMeetingResponse.GetMonthlyMeetingParticipantScheduleDto>> getMonthlyMeetingParticipantSchedules(
            @PathVariable Long meetingScheduleId,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam String tag,
            @RequestParam String nickname
    ) {
        return ResponseDto.onSuccess(guestMeetingUsecase.getMonthlyMeetingParticipantSchedules(meetingScheduleId, year, month, tag, nickname));
    }

}
