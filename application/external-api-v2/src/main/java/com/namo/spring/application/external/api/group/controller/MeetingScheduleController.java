package com.namo.spring.application.external.api.group.controller;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.group.dto.MeetingScheduleRequest;
import com.namo.spring.application.external.api.group.dto.MeetingScheduleResponse;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.application.external.global.utils.Converter;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "7. Schedule (모임) 일정", description = "모임 일정 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/group/schedules")
public class MeetingScheduleController {
	private final Converter converter;

	@Operation(summary = "모임 일정 생성", description = "모임 일정 생성 API")
	@PostMapping("")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<String> createMeetingSchedule(
		@Valid @RequestBody MeetingScheduleRequest.CreateMeetingScheduleDto request
	) {
		return ResponseDto.onSuccess("모임 일정 생성 성공");
	}

	@Operation(summary = "모임 일정 수정", description = "모임 일정 수정 API")
	@PatchMapping("")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<String> modifyMeetingSchedule(
		@Valid @RequestBody MeetingScheduleRequest.UpdateMeetingScheduleDto request
	) {
		return ResponseDto.onSuccess("모임 일정 수정 성공");
	}

	@Operation(summary = "모임 일정 카테고리 수정", description = "모임 일정 카테고리 수정 API")
	@PatchMapping("/category")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<String> modifyMeetingScheduleCategory(
		@Valid @RequestBody MeetingScheduleRequest.UpdateMeetingScheduleCategoryDto request,
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		return ResponseDto.onSuccess("모임 일정 카테고리 수정 성공");
	}

	@Operation(summary = "모임 일정 삭제", description = "모임 일정 삭제 API")
	@DeleteMapping("/{groupScheduleId}")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<String> removeMeetingSchedule(
		@Parameter(description = "모임 일정 ID") @PathVariable Long groupScheduleId,
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		return ResponseDto.onSuccess("모임 일정 삭제 성공");
	}

	@Operation(summary = "월간 모임 일정 조회", description = "월간 모임 일정 조회 API")
	@GetMapping("/{groupId}/{month}")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<MeetingScheduleResponse.MonthlyMeetingScheduleDto> getMonthMeetingSchedules(
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
	public ResponseDto<MeetingScheduleResponse.AllMeetingScheduleDto> getAllMeetingSchedules(
		@Parameter(description = "그룹 ID") @PathVariable("groupId") Long groupId,
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		return null;
	}

	@Operation(summary = "모임 일정 생성 알림", description = "모임 일정 생성 알림 API")
	@PostMapping("/alarm")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<Void> createMeetingScheduleAlarm(
		@Valid @RequestBody MeetingScheduleRequest.CreateMeetingScheduleAlarmDto request,
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		return null;
	}

	@Operation(summary = "모임 일정 변경 알림", description = "모임 일정 변경 알림 API")
	@PatchMapping("/alarm")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<Void> modifyMeetingScheduleAlarm(
		@Valid @RequestBody MeetingScheduleRequest.UpdateMeetingScheduleAlarmDto request,
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		return null;
	}
}
