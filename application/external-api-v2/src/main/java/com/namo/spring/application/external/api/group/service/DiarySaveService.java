package com.namo.spring.application.external.api.group.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.group.converter.DiaryConverter;
import com.namo.spring.application.external.api.group.dto.MeetingDiaryRequest;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.diary.entity.Diary;
import com.namo.spring.db.mysql.domains.diary.exception.DiaryException;
import com.namo.spring.db.mysql.domains.diary.service.DiaryService;
import com.namo.spring.db.mysql.domains.schedule.entity.MeetingSchedule;
import com.namo.spring.db.mysql.domains.schedule.exception.MeetingScheduleException;
import com.namo.spring.db.mysql.domains.schedule.service.MeetingScheduleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiarySaveService {
	private final DiaryService diaryService;
	private final MeetingScheduleService meetingScheduleService;

	@Transactional
	public void saveMeetingDiary(
		Long meetingScheduleId,
		MeetingDiaryRequest.MeetingMemoDto meetingMemoDto
	) {
		MeetingSchedule meetingSchedule = meetingScheduleService.readMeetingSchedule(meetingScheduleId).orElseThrow(
			() -> new MeetingScheduleException(ErrorStatus.NOT_FOUND_MEETING_SCHEDULE_FAILURE)
		);
		diaryService.createDiary(DiaryConverter.toDiary(meetingSchedule, meetingMemoDto.getText()));
	}

	@Transactional
	public void updateMeetingDiary(
		Long meetingScheduleId,
		MeetingDiaryRequest.MeetingMemoDto meetingMemoDto
	) {
		MeetingSchedule meetingSchedule = meetingScheduleService.readMeetingSchedule(meetingScheduleId).orElseThrow(
			() -> new MeetingScheduleException(ErrorStatus.NOT_FOUND_MEETING_SCHEDULE_FAILURE)
		);
		Diary diary = diaryService.readDiaryByMeetingSchedule(meetingSchedule).orElseThrow(
			() -> new DiaryException(ErrorStatus.NOT_FOUND_DIARY_FAILURE)
		);

		diary.update(meetingMemoDto.getText());
	}
}
