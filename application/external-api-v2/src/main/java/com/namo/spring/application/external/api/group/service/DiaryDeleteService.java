package com.namo.spring.application.external.api.group.service;

import org.springframework.stereotype.Service;

import com.namo.spring.db.mysql.domains.diary.service.DiaryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiaryDeleteService {
	private final DiaryService diaryService;

	public void deleteByMeetingSchedule(Long meetingScheduleId) {
		diaryService.deleteDiaryByMeetingSchedule(meetingScheduleId);
	}
}
