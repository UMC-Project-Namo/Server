package com.namo.spring.application.external.api.group.converter;

import java.util.List;

import org.springframework.data.domain.Page;

import com.namo.spring.application.external.api.group.dto.MeetingDiaryResponse;
import com.namo.spring.core.common.utils.DateUtil;
import com.namo.spring.db.mysql.domains.diary.entity.ActivityImg;
import com.namo.spring.db.mysql.domains.diary.entity.Diary;
import com.namo.spring.db.mysql.domains.schedule.entity.MeetingSchedule;

public class MeetingDiaryResponseConverter {
	private MeetingDiaryResponseConverter() {
		throw new IllegalStateException("Util Class");
	}

	public static MeetingDiaryResponse.MonthlyMeetingDiaryInfoDto toMonthlyMeetingDiaryInfoDto(
		MeetingSchedule meetingSchedule,
		Diary diary,
		List<ActivityImg> activityImgs
	) {
		List<MeetingDiaryResponse.MonthlyDiaryInfoImageDto> images = activityImgs.stream().map(img ->
			MeetingDiaryResponse.MonthlyDiaryInfoImageDto.builder()
				.id(img.getId())
				.url(img.getImgUrl())
				.build()
		).toList();

		return MeetingDiaryResponse.MonthlyMeetingDiaryInfoDto.builder()
			.scheduleId(meetingSchedule.getId())
			.name(meetingSchedule.getCustomTitle())
			.startDate(DateUtil.toSeconds(meetingSchedule.getSchedule().getPeriod().getStartDate()))
			.contents(diary.getMemo())
			.images(images)
			.categoryId(meetingSchedule.getCategory().getId())
			.color((long)meetingSchedule.getCategory().getPalette().getColor())
			.placeName(meetingSchedule.getSchedule().getLocation().getName())
			.build();
	}

	public static MeetingDiaryResponse.MonthlyMeetingDiaryDto toMonthlyMeetingDiaryDto(
		List<MeetingDiaryResponse.MonthlyMeetingDiaryInfoDto> diaryDetails,
		Page<MeetingSchedule> meetingSchedule
	) {
		return MeetingDiaryResponse.MonthlyMeetingDiaryDto.builder()
			.content(diaryDetails)
			.currentPage(meetingSchedule.getPageable().getPageNumber())
			.size(meetingSchedule.getPageable().getPageSize())
			.first(meetingSchedule.isFirst())
			.last(meetingSchedule.isLast())
			.build();

	}
}
