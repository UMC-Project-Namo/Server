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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/schedules")
public class ScheduleController {

	private final ScheduleUsecase scheduleUsecase;

	@GetMapping("/{scheduleId}")
	public ResponseDto<ScheduleResponse.ScheduleSummaryDto> getScheduleSummary(
		@AuthenticationPrincipal SecurityUserDetails member,
		@PathVariable String scheduleId) {

		return ResponseDto.onSuccess(null);
	}

}
