package com.namo.spring.application.external.api.group.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.diary.entity.Diary;
import com.namo.spring.db.mysql.domains.diary.exception.DiaryException;
import com.namo.spring.db.mysql.domains.diary.service.DiaryService;
import com.namo.spring.db.mysql.domains.schedule.entity.MeetingSchedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiarySearchService {
	private final DiaryService diaryService;

	@Transactional(readOnly = true)
	public Diary readDiaryByMeetingSchedule(MeetingSchedule meetingSchedule) {
		return diaryService.readDiaryByMeetingSchedule(meetingSchedule).orElseThrow(
			() -> new DiaryException(ErrorStatus.NOT_FOUND_DIARY_FAILURE)
		);
	}
}
