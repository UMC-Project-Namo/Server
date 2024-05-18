package com.example.namo2.domain.group.ui;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.namo2.domain.group.application.MoimScheduleFacade;
import com.example.namo2.domain.group.ui.dto.GroupScheduleRequest;
import com.example.namo2.domain.group.ui.dto.GroupScheduleResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.namo2.global.annotation.swagger.ApiErrorCodes;
import com.example.namo2.global.common.response.BaseResponse;
import com.example.namo2.global.common.response.BaseResponseStatus;
import com.example.namo2.global.utils.Converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "7. Schedule (모임)", description = "모임 일정 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/group/schedules")
public class GroupScheduleController {
	private final MoimScheduleFacade moimScheduleFacade;
	private final Converter converter;

	@Operation(summary = "모임 일정 생성", description = "모임 일정 생성 API")
	@PostMapping("")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Long> createMoimSchedule(
		@Valid @RequestBody GroupScheduleRequest.PostGroupScheduleDto scheduleReq
	) {
		Long scheduleId = moimScheduleFacade.createSchedule(scheduleReq);
		return new BaseResponse(scheduleId);
	}

	@Operation(summary = "모임 일정 수정", description = "모임 일정 수정 API")
	@PatchMapping("")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Long> modifyMoimSchedule(
		@Valid @RequestBody GroupScheduleRequest.PatchGroupScheduleDto scheduleReq
	) {
		moimScheduleFacade.modifyMoimSchedule(scheduleReq);
		return BaseResponse.ok();
	}

	@Operation(summary = "모임 일정 카테고리 수정", description = "모임 일정 카테고리 수정 API")
	@PatchMapping("/category")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Long> modifyMoimScheduleCategory(
		@Valid @RequestBody GroupScheduleRequest.PatchGroupScheduleCategoryDto scheduleReq,
		HttpServletRequest request
	) {
		moimScheduleFacade.modifyMoimScheduleCategory(scheduleReq, (Long)request.getAttribute("userId"));
		return BaseResponse.ok();
	}

	@Operation(summary = "모임 일정 삭제", description = "모임 일정 삭제 API")
	@DeleteMapping("/{moimScheduleId}")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Long> removeMoimSchedule(
		@Parameter(description = "모임 일정 ID") @PathVariable Long moimScheduleId,
		HttpServletRequest request
	) {
		moimScheduleFacade.removeMoimSchedule(moimScheduleId, (Long)request.getAttribute("userId"));
		return BaseResponse.ok();
	}

	@Operation(summary = "월간 모임 일정 조회", description = "월간 모임 일정 조회 API")
	@GetMapping("/{groupId}/{month}")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<GroupScheduleResponse.MoimScheduleDto> getMonthMoimSchedules(
		@Parameter(description = "그룹 ID") @PathVariable("groupId") Long groupId,
		@Parameter(description = "조회 일자", example = "{년},{월}") @PathVariable("month") String month,
		HttpServletRequest request
	) {
		List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
		List<GroupScheduleResponse.MoimScheduleDto> schedules = moimScheduleFacade.getMonthMoimSchedules(groupId,
			localDateTimes, (Long)request.getAttribute("userId"));
		return new BaseResponse(schedules);
	}

	@Operation(summary = "모든 모임 일정 조회", description = "모든 모임 일정 조회 API")
	@GetMapping("/{groupId}/all")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<GroupScheduleResponse.MoimScheduleDto> getAllMoimSchedules(
		@Parameter(description = "그룹 ID") @PathVariable("groupId") Long groupId,
		HttpServletRequest request
	) {
		List<GroupScheduleResponse.MoimScheduleDto> schedules
			= moimScheduleFacade.getAllMoimSchedules(groupId, (Long)request.getAttribute("userId"));
		return new BaseResponse(schedules);
	}

	@Operation(summary = "모임 일정 생성 알람", description = "모임 일정 생성 알람 API")
	@PostMapping("/alarm")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse createMoimScheduleAlarm(
		@Valid @RequestBody GroupScheduleRequest.PostGroupScheduleAlarmDto postGroupScheduleAlarmDto,
		HttpServletRequest request
	) {
		moimScheduleFacade.createMoimScheduleAlarm(postGroupScheduleAlarmDto, (Long)request.getAttribute("userId"));
		return BaseResponse.ok();
	}

	@Operation(summary = "모임 일정 변경 알람", description = "모임 일정 변경 알람 API")
	@PatchMapping("/alarm")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse modifyMoimScheduleAlarm(
		@Valid @RequestBody GroupScheduleRequest.PostGroupScheduleAlarmDto postGroupScheduleAlarmDto,
		HttpServletRequest request
	) {
		moimScheduleFacade.modifyMoimScheduleAlarm(postGroupScheduleAlarmDto, (Long)request.getAttribute("userId"));
		return BaseResponse.ok();
	}
}
