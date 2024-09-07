package com.namo.spring.application.external.api.schedule.usecase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.schedule.converter.ScheduleConverter;
import com.namo.spring.application.external.api.schedule.dto.ScheduleResponse;
import com.namo.spring.application.external.api.schedule.service.ParticipantManageService;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScheduleUsecase {

	private final ParticipantManageService participantManageService;

	@Transactional(readOnly = true)
	public ScheduleResponse.ScheduleSummaryDto getScheduleSummary(Long memberId, Long scheduleId) {
		Participant myScheduleParticipant = participantManageService.getMyParticipant(memberId, scheduleId);
		return ScheduleConverter.toScheduleSummaryDto(myScheduleParticipant);
	}
}
