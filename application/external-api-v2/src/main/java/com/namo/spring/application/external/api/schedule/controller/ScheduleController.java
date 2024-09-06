package com.namo.spring.application.external.api.schedule.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		return ResponseDto.onSuccess(scheduleUsecase.getScheduleSummary(scheduleId,
			member.getUserId()));
	}

}
