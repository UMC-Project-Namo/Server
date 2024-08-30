package com.namo.spring.db.mysql.domains.record.service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.record.entity.Diary;
import com.namo.spring.db.mysql.domains.record.entity.DiaryImage;
import com.namo.spring.db.mysql.domains.record.repository.DiaryImageRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class DiaryImageService {
	private final DiaryImageRepository diaryImageRepository;

	@Transactional
	public DiaryImage createDiaryImage(DiaryImage diaryImage) {
		return diaryImageRepository.save(diaryImage);
	}

	@Transactional(readOnly = true)
	public Optional<DiaryImage> readDiaryImage(Long diaryImageId) {
		return diaryImageRepository.findById(diaryImageId);
	}

	@Transactional
	public void deleteDiaryImage(Long diaryImageId) {
		diaryImageRepository.deleteById(diaryImageId);
	}

	public void deleteAll(Diary diary) {
		diaryImageRepository.deleteAllByDiary(diary);
	}
}
