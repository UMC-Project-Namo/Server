package com.namo.spring.db.mysql.domains.diary.service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.diary.entity.DiaryImg;
import com.namo.spring.db.mysql.domains.diary.repository.DiaryImgRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class DiaryImgService {
	private final DiaryImgRepository diaryImgRepository;

	@Transactional
	public DiaryImg createDiaryImg(DiaryImg diaryImg) {
		return diaryImgRepository.save(diaryImg);
	}

	@Transactional(readOnly = true)
	public Optional<DiaryImg> readDiaryImg(Long diaryImgId) {
		return diaryImgRepository.findById(diaryImgId);
	}

	@Transactional
	public void deleteDiaryImg(Long diaryImgId) {
		diaryImgRepository.deleteById(diaryImgId);
	}

}
