package com.namo.spring.application.external.api.group.controller;

import com.namo.spring.application.external.api.group.dto.MeetingScheduleRequest;
import com.namo.spring.application.external.api.group.dto.MeetingScheduleResponse;
import com.namo.spring.application.external.api.group.facade.MeetingScheduleFacade;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.application.external.global.utils.Converter;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "7. Schedule (모임) - 네임 규칙 적용", description = "모임 일정 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/temp/group/schedules")
public class TempMeetingScheduleController {
    private final MeetingScheduleFacade meetingScheduleFacade;
    private final Converter converter;

    @Operation(summary = "모임 일정 수정", description = "모임 일정 수정 API")
    @PatchMapping("")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<Long> modifyMeetingSchedule(
            @Valid @RequestBody MeetingScheduleRequest.PatchMeetingScheduleDto scheduleReq
    ) {
        meetingScheduleFacade.modifyMeetingSchedule(scheduleReq);
        return ResponseDto.onSuccess(null);
    }

    @Operation(summary = "모임 일정 카테고리 수정", description = "모임 일정 카테고리 수정 API")
    @PatchMapping("/category")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<Long> modifyMeetingScheduleCategory(
            @Valid @RequestBody MeetingScheduleRequest.PatchMeetingScheduleCategoryDto scheduleReq,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        meetingScheduleFacade.modifyMeetingScheduleCategory(scheduleReq, user.getUserId());
        return ResponseDto.onSuccess(null);
    }

    @Operation(summary = "월간 모임 일정 조회", description = "월간 모임 일정 조회 API")
    @GetMapping("/{groupId}/{month}")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<List<MeetingScheduleResponse.MeetingScheduleDto>> getMonthMeetingSchedules(
            @Parameter(description = "그룹 ID") @PathVariable("groupId") Long groupId,
            @Parameter(description = "조회 일자", example = "{년},{월}") @PathVariable("month") String month,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
        List<MeetingScheduleResponse.MeetingScheduleDto> schedules = meetingScheduleFacade.getMonthMeetingSchedules(
                groupId,
                localDateTimes, user.getUserId());
        return ResponseDto.onSuccess(schedules);
    }

    @Operation(summary = "모든 모임 일정 조회", description = "모든 모임 일정 조회 API")
    @GetMapping("/{groupId}/all")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<List<MeetingScheduleResponse.MeetingScheduleDto>> getAllMeetingSchedules(
            @Parameter(description = "그룹 ID") @PathVariable("groupId") Long groupId,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        List<MeetingScheduleResponse.MeetingScheduleDto> schedules
                = meetingScheduleFacade.getAllMeetingSchedules(groupId, user.getUserId());
        return ResponseDto.onSuccess(schedules);
    }

    @Operation(summary = "모임 일정 삭제", description = "모임 일정 삭제 API")
    @DeleteMapping("/{meetingScheduleId}")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<Long> removeMeetingSchedule(
            @Parameter(description = "모임 일정 ID") @PathVariable Long meetingScheduleId,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        meetingScheduleFacade.removeMeetingSchedule(meetingScheduleId, user.getUserId());
        return ResponseDto.onSuccess(null);
    }

    @Operation(summary = "모임 일정 생성 알람", description = "모임 일정 생성 알람 API")
    @PostMapping("/alarm")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<Void> createMeetingScheduleAlarm(
            @Valid @RequestBody MeetingScheduleRequest.PostMeetingScheduleAlarmDto postMeetingScheduleAlarmDto,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        meetingScheduleFacade.createMeetingScheduleAlarm(postMeetingScheduleAlarmDto, user.getUserId());
        return ResponseDto.onSuccess(null);
    }

    @Operation(summary = "모임 일정 변경 알람", description = "모임 일정 변경 알람 API")
    @PatchMapping("/alarm")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<Void> modifyMeetingScheduleAlarm(
            @Valid @RequestBody MeetingScheduleRequest.PostMeetingScheduleAlarmDto postMeetingScheduleAlarmDto,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        meetingScheduleFacade.modifyMeetingScheduleAlarm(postMeetingScheduleAlarmDto, user.getUserId());
        return ResponseDto.onSuccess(null);
    }
}
