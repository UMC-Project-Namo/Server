package com.namo.spring.application.external.api.guest.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.guest.dto.GuestMeetingResponse;
import com.namo.spring.application.external.api.guest.dto.GuestParticipantRequest;
import com.namo.spring.application.external.api.guest.dto.GuestParticipantResponse;
import com.namo.spring.application.external.api.guest.usecase.GuestUsecase;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "게스트", description = "게스트 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/guests")
@PreAuthorize("isAnonymous()")
public class GuestController {
    private final GuestUsecase guestUsecase;

    /**
     * 게스트 로그인 / 모임 가입 API
     */
    @PostMapping(path = "/invitations")
    public ResponseDto<GuestParticipantResponse.PostGuestParticipantDto> guestMeetingAccess(
            @RequestParam String code,
            @Valid @RequestBody GuestParticipantRequest.PostGuestParticipantDto dto) {
        return ResponseDto.onSuccess(guestUsecase.createOrValidateGuest(dto, code));
    }

    /**
     * 게스트 모임 일정 월간 조회 API
     */
    @GetMapping(path = "/meeting/{meetingScheduleId}/calendar")
    public ResponseDto<List<GuestMeetingResponse.GetMonthlyMeetingParticipantScheduleDto>> getMonthlyMeetingParticipantSchedules(
            @PathVariable Long meetingScheduleId,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam String tag,
            @RequestParam String nickname
    ) {
        return ResponseDto.onSuccess(
                guestUsecase.getMonthlyMeetingParticipantSchedules(meetingScheduleId, year, month, tag, nickname));
    }

}
