package com.namo.spring.application.external.api.group.usecase;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.group.converter.MeetingDiaryResponseConverter;
import com.namo.spring.application.external.api.group.dto.MeetingDiaryRequest;
import com.namo.spring.application.external.api.group.dto.MeetingDiaryResponse;
import com.namo.spring.application.external.api.group.service.ActivityImgSearchService;
import com.namo.spring.application.external.api.group.service.DiaryDeleteService;
import com.namo.spring.application.external.api.group.service.DiarySaveService;
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

	private final DiarySaveService diarySaveService;
	private final DiarySearchService diarySearchService;
	private final DiaryDeleteService diaryDeleteService;

	private final ActivityImgSearchService activityImgSearchService;

	/**
	 * 개인 페이지
	 */
	@Transactional
	public void createPersonalMeetingDiary(
		Long meetingScheduleId,
		MeetingDiaryRequest.MeetingMemoDto meetingMemoDto
	) {
		diarySaveService.saveMeetingDiary(meetingScheduleId, meetingMemoDto);
	}

	@Transactional
	public void updatePersonalMeetingDiary(
		Long meetingScheduleId,
		MeetingDiaryRequest.MeetingMemoDto meetingMemoDto
	) {
		diarySaveService.updateMeetingDiary(meetingScheduleId, meetingMemoDto);
	}

	@Transactional(readOnly = true)
	public MeetingDiaryResponse.DiaryDetailDto getPersonalMeetingDiaryDetail(Long meetingScheduleId) {
		MeetingSchedule meetingSchedule = meetingScheduleSearchService.readMeetingSchedule(meetingScheduleId);
		Diary diary = diarySearchService.readDiaryByMeetingSchedule(meetingSchedule);
		List<ActivityImg> activityImgs = activityImgSearchService.readAllByDiary(diary);

		return MeetingDiaryResponseConverter.toDiaryDetailDto(meetingSchedule, diary, activityImgs);
	}

	@Transactional(readOnly = true)
	public MeetingDiaryResponse.SliceDiaryDto getPersonalMeetingDiaryByMonth(
		Integer year,
		Integer month,
		Pageable page
	) {
		Page<MeetingSchedule> meetingSchedules = meetingScheduleSearchService.readAllByMonth(year, month, page);
		List<MeetingDiaryResponse.DiaryDetailDto> diaryDetails = meetingSchedules.stream().map(meetingSchedule ->
			{
				Diary diary = diarySearchService.readDiaryByMeetingSchedule(meetingSchedule);
				List<ActivityImg> activityImgs = activityImgSearchService.readAllByDiary(diary);
				return MeetingDiaryResponseConverter.toDiaryDetailDto(meetingSchedule, diary, activityImgs);
			}
		).toList();

		return MeetingDiaryResponseConverter.toSliceDiaryDto(diaryDetails, meetingSchedules);
	}

	@Transactional
	public void deletePersonalMeetingDiary(Long meetingScheduleId) {
		diaryDeleteService.deleteByMeetingSchedule(meetingScheduleId);
	}

}
