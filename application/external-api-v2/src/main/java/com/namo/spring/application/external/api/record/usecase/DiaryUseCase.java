package com.namo.spring.application.external.api.record.usecase;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.record.converter.DiaryResponseConverter;
import com.namo.spring.application.external.api.record.dto.DiaryRequest;
import com.namo.spring.application.external.api.record.dto.DiaryResponse;
import com.namo.spring.application.external.api.record.serivce.DiaryManageService;
import com.namo.spring.application.external.api.schedule.service.ParticipantManageService;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.db.mysql.domains.record.entity.Diary;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DiaryUseCase {

	private final DiaryManageService diaryManageService;
	private final ParticipantManageService participantManageService;

	@Transactional
	public void createDiary(SecurityUserDetails memberInfo, DiaryRequest.CreateDiaryDto request) {
		Participant participant = participantManageService.getScheduleParticipant(memberInfo.getUserId(),
			request.getScheduleId());
		diaryManageService.makeDiary(request, participant);
	}

	@Transactional
	public void updateDiary(Long diaryId, SecurityUserDetails memberInfo, DiaryRequest.UpdateDiaryDto request) {
		Diary diary = diaryManageService.getMyDiary(diaryId, memberInfo.getUserId());
		diaryManageService.updateDiary(diary, request);
	}

	@Transactional(readOnly = true)
	public DiaryResponse.DiaryDto getScheduleDiary(Long scheduleId, SecurityUserDetails memberInfo) {
		Participant participant = participantManageService.getScheduleParticipant(memberInfo.getUserId(),
			scheduleId);
		Diary diary = diaryManageService.getParticipantDiary(participant);
		return DiaryResponseConverter.toDiaryDto(diary);
	}

	@Transactional(readOnly = true)
	public List<DiaryResponse.DiaryArchiveDto> getDiaryArchiveDto(Long memberId, Pageable pageable) {
		List<Participant> allMyParticipant = participantManageService.getMyParticipation(memberId, pageable);
		return allMyParticipant.stream()
			.map(DiaryResponseConverter::toDiaryArchiveDto)
			.toList();
	}
}
