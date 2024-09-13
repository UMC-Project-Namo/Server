package com.namo.spring.application.external.api.guest.controller;

import java.util.List;

import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.core.common.code.status.ErrorStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Tag(name = "게스트", description = "게스트 관련 API, authorization 헤더에 값을 넣지 않습니다.")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/guests")
@PreAuthorize("isAnonymous()")
public class GuestController {
    private final GuestUsecase guestUsecase;

    @Operation(summary = "게스트 로그인 / 모임 참여", description = "참여 코드에 대하여 게스트 접근을 허용합니다. 참여 코드와 닉네임이 존재할 시 게스트 로그인을, 존재 하지 않을 시에는 모임에 참여합니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.NOT_FOUND_SCHEDULE_FAILURE,
            ErrorStatus.NOT_MEETING_SCHEDULE,
            ErrorStatus.ANONYMOUS_LOGIN_FAILURE,
            ErrorStatus.NOT_SCHEDULE_PARTICIPANT,
            ErrorStatus.NOT_FOUND_COLOR
    })
    @PostMapping(path = "/invitations")
    public ResponseDto<GuestParticipantResponse.PostGuestParticipantDto> guestMeetingAccess(
            @Parameter(description = "참여 코드, 웹 링크의 code 파라미터를 입력합니다.") @RequestParam String code,
            @Valid @RequestBody GuestParticipantRequest.PostGuestParticipantDto dto) {
        return ResponseDto.onSuccess(guestUsecase.createOrValidateGuest(dto, code));
    }

    @Operation(summary = "모임 일정 월간 조회")
    @ApiErrorCodes(value = {
            ErrorStatus.INVALID_FORMAT_FAILURE,
            ErrorStatus.NOT_MEETING_SCHEDULE,
            ErrorStatus.NOT_FOUND_SCHEDULE_FAILURE,
            ErrorStatus.NOT_FOUND_USER_FAILURE,
            ErrorStatus.NOT_SCHEDULE_PARTICIPANT,
    })
    @GetMapping(path = "/meeting/{meetingScheduleId}/calendar")
    public ResponseDto<List<GuestMeetingResponse.GetMonthlyMeetingParticipantScheduleDto>> getMonthlyMeetingParticipantSchedules(
        @Parameter(description ="모임 일정 ID")@PathVariable Long meetingScheduleId,
        @Parameter(description ="연도")@RequestParam Integer year,
        @Parameter(description ="월")@RequestParam Integer month,
        @Parameter(description ="게스트 태그")@RequestParam String tag,
        @Parameter(description ="게스트 닉네임")@RequestParam String nickname
    ) {
        return ResponseDto.onSuccess(
                guestUsecase.getMonthlyMeetingParticipantSchedules(meetingScheduleId, year, month, tag, nickname));
    }

}
