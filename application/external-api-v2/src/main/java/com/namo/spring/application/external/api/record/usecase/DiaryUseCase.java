package com.namo.spring.application.external.api.record.usecase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.record.dto.DiaryRequest;
import com.namo.spring.application.external.api.record.serivce.DiaryManageService;
import com.namo.spring.application.external.api.schedule.service.ParticipantManageService;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DiaryUseCase {

	private final DiaryManageService diaryManageService;
	private final ParticipantManageService participantManageService;

	@Transactional
	public void createDiary(SecurityUserDetails memberInfo, DiaryRequest.CreateDiaryDto request) {
		Participant participant = participantManageService.getParticipantForDiary(memberInfo.getUserId(),
			request.getScheduleId());
		diaryManageService.makeDiary(request, participant);
	}
}
