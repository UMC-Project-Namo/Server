package com.namo.spring.application.external.api.schedule.controller;

import static com.namo.spring.core.common.code.status.ErrorStatus.*;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.namo.spring.application.external.api.schedule.dto.ScheduleResponse;
import com.namo.spring.application.external.api.schedule.usecase.ScheduleUsecase;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "공통 일정", description = "공통 일정 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/schedules")
public class ScheduleController {
    private final ScheduleUsecase scheduleUsecase;

    @Operation(summary = "기록 상단부 스케줄 조회 API", description = "스케줄에 대한 제목, 날짜, 장소 정보를 반환합니다.")
    @GetMapping("/{scheduleId}")
    public ResponseDto<ScheduleResponse.ScheduleSummaryDto> getScheduleSummary(
            @AuthenticationPrincipal SecurityUserDetails member,
            @PathVariable Long scheduleId) {
        return ResponseDto.onSuccess(scheduleUsecase.getScheduleSummary(
                member.getUserId(), scheduleId));
    }

    @Operation(summary = "일정 알림 추가/수정/삭제", description = "일정 알림을 추가/수정/삭제 합니다. " +
            "최종적으로 일정에 설정할 알림을 array로 전송하며, 알림을 모두 삭제할 때에는 empty array를 전송합니다.")
    @ApiErrorCodes(value = {
            NOT_FOUND_USER_FAILURE,
            NOT_FOUND_SCHEDULE_FAILURE,
            NOT_SCHEDULE_PARTICIPANT,
            NOT_FOUND_MOBILE_DEVICE_FAILURE,
            NOT_SUPPORTED_DEVICE_TYPE
    })
    @PutMapping("/{scheduleId}/notifications")
    public ResponseDto<String> updateScheduleReminder(@Parameter(description = "일정 ID") @PathVariable Long scheduleId,
                                                      @Parameter(description = "일정 알림 수정 요청 dto") @Valid @RequestBody ScheduleRequest.PutScheduleReminderDto dto,
                                                      @AuthenticationPrincipal SecurityUserDetails member) {
        scheduleUsecase.updateOrCreateScheduleReminder(dto, scheduleId, member);
        return ResponseDto.onSuccess("알림 수정 성공");
    }
}
