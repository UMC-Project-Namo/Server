package com.namo.spring.application.external.api.record.serivce;

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

	public void makeDiary(DiaryRequest.CreateDiaryDto request, Participant participant) {
		Diary diary = Diary.of(participant, request.getContent(), request.getEnjoyRating());
		diaryService.createDiary(diary);

		if (!request.getDiaryImages().isEmpty()) {
			request.getDiaryImages().forEach(diaryImage -> {
				DiaryImage image = DiaryImage.of(diary, diaryImage.getImageUrl(), diaryImage.getOrderNumber());
				diaryImageService.createDiaryImage(image);
			});
		}
	}
}
