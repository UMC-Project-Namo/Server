package com.namo.spring.application.external.api.group.usecase;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.group.converter.MeetingDiaryResponseConverter;
import com.namo.spring.application.external.api.group.dto.MeetingDiaryResponse;
import com.namo.spring.application.external.api.group.service.ActivityImgSearchService;
import com.namo.spring.application.external.api.group.service.DiarySearchService;
import com.namo.spring.application.external.api.group.service.MeetingScheduleSearchService;
import com.namo.spring.db.mysql.domains.diary.entity.ActivityImg;
import com.namo.spring.db.mysql.domains.diary.entity.Diary;
import com.namo.spring.db.mysql.domains.schedule.entity.MeetingSchedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeetingDiaryUseCase {
	private final MeetingScheduleSearchService meetingScheduleSearchService;
	private final DiarySearchService diarySearchService;

	private final ActivityImgSearchService activityImgSearchService;

	@Transactional(readOnly = true)
	public MeetingDiaryResponse.DiaryDetailDto getPersonalMeetingDiaryDetail(Long meetingScheduleId) {
		MeetingSchedule meetingSchedule = meetingScheduleSearchService.readMeetingSchedule(meetingScheduleId);
		Diary diary = diarySearchService.readDiaryByMeetingSchedule(meetingSchedule);
		List<ActivityImg> activityImgs = activityImgSearchService.readAllByDiary(diary);

		return MeetingDiaryResponseConverter.toDiaryDetailDto(meetingSchedule, diary, activityImgs);
	}
}
