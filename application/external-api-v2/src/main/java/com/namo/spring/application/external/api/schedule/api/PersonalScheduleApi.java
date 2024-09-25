package com.namo.spring.application.external.api.schedule.api;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.namo.spring.application.external.api.schedule.dto.PersonalScheduleRequest;
import com.namo.spring.application.external.api.schedule.dto.PersonalScheduleResponse;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "개인 일정", description = "개인 일정 관련 API")
public interface PersonalScheduleApi {
    @Operation(summary = "개인 일정 생성", description = "개인 일정을 생성합니다. 요청 성공 시 개인 일정 ID를 전송합니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.NOT_FOUND_USER_FAILURE,
            ErrorStatus.NOT_FOUND_CATEGORY_FAILURE,
            ErrorStatus.INVALID_DATE})
    ResponseDto<Long> createPersonalSchedule(
            @Parameter(description = "개인 일정 생성 요청 dto") @Valid @RequestBody PersonalScheduleRequest.PostPersonalScheduleDto dto,
            @AuthenticationPrincipal SecurityUserDetails member) throws JsonProcessingException;

    @Operation(summary = "개인 월간 일정 조회", description = "개인 월간 일정을 조회합니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.INVALID_FORMAT_FAILURE})
    ResponseDto<List<PersonalScheduleResponse.GetMonthlyScheduleDto>> getMyMonthlySchedules(
            @Parameter(description = "연도") @RequestParam Integer year,
            @Parameter(description = "월") @RequestParam Integer month,
            @AuthenticationPrincipal SecurityUserDetails member);

    @Operation(summary = "개인 월간 일정 - 친구 생일 조회", description = "월간 친구 생일을 조회합니다.")
    ResponseDto<List<PersonalScheduleResponse.GetMonthlyFriendBirthdayDto>> getMonthlyFriendsBirthday(
            @Parameter(description = "연도") @RequestParam Integer year,
            @Parameter(description = "월") @RequestParam Integer month,
            @AuthenticationPrincipal SecurityUserDetails member);

    @Operation(summary = "친구 월간 일정 조회", description = "친구의 월간 일정을 조회합니다.")
    @ApiErrorCodes(value = {ErrorStatus.NOT_FRIENDSHIP_MEMBER})
    ResponseDto<List<PersonalScheduleResponse.GetFriendMonthlyScheduleDto>> getFriendMonthlySchedules(
            @Parameter(description = "연도") @RequestParam Integer year,
            @Parameter(description = "월") @RequestParam Integer month,
            @Parameter(description = "친구 유저 ID") @RequestParam Long memberId,
            @AuthenticationPrincipal SecurityUserDetails member);

    @Operation(summary = "개인 일정 내용 수정", description = "개인 일정 내용을 수정합니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.INVALID_DATE,
            ErrorStatus.NOT_SCHEDULE_OWNER,
            ErrorStatus.NOT_FOUND_SCHEDULE_FAILURE,
            ErrorStatus.NOT_PERSONAL_SCHEDULE
    })
    ResponseDto<String> updatePersonalSchedules(@Parameter(description = "일정 ID") @PathVariable Long scheduleId,
            @Parameter(description = "일정 내용 수정 요청 dto") @Valid @RequestBody PersonalScheduleRequest.PatchPersonalScheduleDto dto,
            @AuthenticationPrincipal SecurityUserDetails member);
}
