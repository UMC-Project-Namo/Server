package com.namo.spring.application.external.api.record.serivce;

import java.util.List;

import org.springframework.stereotype.Component;

import com.namo.spring.application.external.api.record.dto.DiaryRequest;
import com.namo.spring.db.mysql.domains.record.entity.Diary;
import com.namo.spring.db.mysql.domains.record.entity.DiaryImage;
import com.namo.spring.db.mysql.domains.record.service.DiaryImageService;
import com.namo.spring.db.mysql.domains.record.service.DiaryService;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DiaryManageService {

	private final DiaryService diaryService;
	private final DiaryImageService diaryImageService;

	public Diary getMyDiary(Long diaryId, Long memberId) {
		Diary diary = diaryService.readDiary(diaryId)
			.orElseThrow();
		if (!diary.getParticipant().getMember().getId().equals(memberId)) {
			throw new IllegalArgumentException("해당 일기에 대한 권한이 없습니다.");
		}
		return diary;
	}

	public void makeDiary(DiaryRequest.CreateDiaryDto request, Participant participant) {
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
