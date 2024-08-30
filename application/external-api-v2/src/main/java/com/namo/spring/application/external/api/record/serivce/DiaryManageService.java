package com.namo.spring.application.external.api.record.serivce;

import java.util.List;

import org.springframework.stereotype.Component;

import com.namo.spring.application.external.api.record.dto.DiaryRequest;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.record.entity.Diary;
import com.namo.spring.db.mysql.domains.record.entity.DiaryImage;
import com.namo.spring.db.mysql.domains.record.exception.DiaryException;
import com.namo.spring.db.mysql.domains.record.service.DiaryImageService;
import com.namo.spring.db.mysql.domains.record.service.DiaryService;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DiaryManageService {

	private final DiaryService diaryService;
	private final DiaryImageService diaryImageService;

	public Diary getMyDiary(Long diaryId, Long memberId) {
		Diary diary = diaryService.readDiary(diaryId)
			.orElseThrow(() -> new DiaryException(ErrorStatus.NOT_FOUND_DIARY_FAILURE));
		if (!diary.getParticipant().getMember().getId().equals(memberId))
			throw new DiaryException(ErrorStatus.NOT_MY_DIARY_FAILURE);
		return diary;
	}

	public Diary getParticipantDiary(Participant participant) {
		if (!participant.isHasDiary())
			throw new DiaryException(ErrorStatus.NOT_WRITTEN_DIARY_FAILURE);
		//Todo: 이후 다이어리를 여러 버전으로 저장하는 기획 변경시 최신 버전을 가져오도록 수정 필요 (현재는 첫번째 다이어리만 가져옴) - 2024.8.25
		return participant.getDiaries().get(0);
	}

	public void makeDiary(DiaryRequest.CreateDiaryDto request, Participant participant) {
		if (participant.isHasDiary())
			throw new MemberException(ErrorStatus.ALREADY_WRITTEN_DIARY_FAILURE);
		Diary diary = Diary.of(participant, request.getContent(), request.getEnjoyRating());
		diaryService.createDiary(diary);
		createDiaryImages(diary, request.getDiaryImages());
	}

	public void updateDiary(Diary diary, DiaryRequest.UpdateDiaryDto request) {
		diary.update(request.getContent(), request.getEnjoyRating());
		diaryImageService.deleteAll(diary);
		createDiaryImages(diary, request.getDiaryImages());
	}

	private void createDiaryImages(Diary diary, List<DiaryRequest.CreateDiaryImageDto> diaryImages) {
		if (!diaryImages.isEmpty()) {
			diaryImages.forEach(diaryImage -> {
				DiaryImage image = DiaryImage.of(diary, diaryImage.getImageUrl(), diaryImage.getOrderNumber());
				diaryImageService.createDiaryImage(image);
			});
		}
	}
}
