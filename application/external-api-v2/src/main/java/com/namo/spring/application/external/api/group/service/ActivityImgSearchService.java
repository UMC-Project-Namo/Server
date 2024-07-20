package com.namo.spring.application.external.api.group.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.db.mysql.domains.diary.entity.ActivityImg;
import com.namo.spring.db.mysql.domains.diary.entity.Diary;
import com.namo.spring.db.mysql.domains.diary.service.ActivityImgService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor

public class ActivityImgSearchService {
	private final ActivityImgService activityImgService;

	@Transactional(readOnly = true)
	public List<ActivityImg> readAllByDiary(Diary diary) {
		return activityImgService.readActivityImgByDiary(diary).get();
	}
}
