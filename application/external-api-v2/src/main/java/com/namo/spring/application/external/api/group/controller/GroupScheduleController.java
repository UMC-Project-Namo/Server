package com.namo.spring.application.external.api.group.controller;

import com.namo.spring.application.external.api.group.dto.GroupScheduleRequest;
import com.namo.spring.application.external.api.group.dto.GroupScheduleResponse;
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

@Tag(name = "7. Schedule (모임)", description = "모임 일정 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/group/schedules")
public class GroupScheduleController {
    private final Converter converter;

    @Operation(summary = "모임 일정 생성", description = "모임 일정 생성 API")
    @PostMapping("")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<Long> createGroupSchedule(
            @Valid @RequestBody GroupScheduleRequest.PostGroupScheduleDto scheduleReq
    ) {
        return null;
    }

    @Operation(summary = "모임 일정 수정", description = "모임 일정 수정 API")
    @PatchMapping("")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<Long> modifyGroupSchedule(
            @Valid @RequestBody GroupScheduleRequest.PatchGroupScheduleDto scheduleReq
    ) {
        return null;
    }

    @Operation(summary = "모임 일정 카테고리 수정", description = "모임 일정 카테고리 수정 API")
    @PatchMapping("/category")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<Long> modifyGroupScheduleCategory(
            @Valid @RequestBody GroupScheduleRequest.PatchGroupScheduleCategoryDto scheduleReq,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        return null;
    }

    @Operation(summary = "모임 일정 삭제", description = "모임 일정 삭제 API")
    @DeleteMapping("/{moimScheduleId}")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<Long> removeGroupSchedule(
            @Parameter(description = "모임 일정 ID") @PathVariable Long groupScheduleId,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        return null;
    }

    @Operation(summary = "월간 모임 일정 조회", description = "월간 모임 일정 조회 API")
    @GetMapping("/{groupId}/{month}")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<GroupScheduleResponse.GroupScheduleDto> getMonthGroupSchedules(
            @Parameter(description = "그룹 ID") @PathVariable("groupId") Long groupId,
            @Parameter(description = "조회 일자", example = "{년},{월}") @PathVariable("month") String month,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        return null;
    }

    @Operation(summary = "모든 모임 일정 조회", description = "모든 모임 일정 조회 API")
    @GetMapping("/{groupId}/all")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<GroupScheduleResponse.GroupScheduleDto> getAllGroupSchedules(
            @Parameter(description = "그룹 ID") @PathVariable("groupId") Long groupId,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        return null;
    }

    @Operation(summary = "모임 일정 생성 알람", description = "모임 일정 생성 알람 API")
    @PostMapping("/alarm")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<Void> createGroupScheduleAlarm(
            @Valid @RequestBody GroupScheduleRequest.PostGroupScheduleAlarmDto postGroupScheduleAlarmDto,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        return null;
    }

    @Operation(summary = "모임 일정 변경 알람", description = "모임 일정 변경 알람 API")
    @PatchMapping("/alarm")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<Void> modifyGroupScheduleAlarm(
            @Valid @RequestBody GroupScheduleRequest.PostGroupScheduleAlarmDto postGroupScheduleAlarmDto,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        return null;
    }
}
