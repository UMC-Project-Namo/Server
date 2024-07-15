package com.namo.spring.db.mysql.domains.diary.service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.diary.entity.Diary;
import com.namo.spring.db.mysql.domains.diary.repository.DiaryRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class DiaryService {
	private final DiaryRepository diaryRepository;

	@Transactional
	public Diary createDiary(Diary diary) {
		return diaryRepository.save(diary);
	}

	@Transactional(readOnly = true)
	public Optional<Diary> readDiary(Long diaryId) {
		return diaryRepository.findById(diaryId);
	}

	@Transactional
	public void deleteDiary(Long diaryId) {
		diaryRepository.deleteById(diaryId);
	}

}
