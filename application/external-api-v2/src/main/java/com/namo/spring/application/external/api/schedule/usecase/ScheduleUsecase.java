package com.namo.spring.application.external.api.schedule.usecase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.schedule.converter.ScheduleConverter;
import com.namo.spring.application.external.api.schedule.dto.ScheduleResponse;
import com.namo.spring.application.external.api.schedule.service.ScheduleManageService;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScheduleUsecase {

	private ScheduleManageService scheduleManageService;

	@Transactional(readOnly = true)
	public ScheduleResponse.ScheduleSummaryDto getScheduleSummary(Long memberId, Long scheduleId) {
		Schedule mySchedule = scheduleManageService.getMySchedule(memberId, scheduleId);
		return ScheduleConverter.toScheduleSummaryDto(mySchedule);
	}
}
