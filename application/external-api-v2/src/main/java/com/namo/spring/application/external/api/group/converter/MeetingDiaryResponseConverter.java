package com.namo.spring.application.external.api.group.converter;

import java.util.List;

import com.namo.spring.application.external.api.group.dto.MeetingDiaryResponse;
import com.namo.spring.core.common.utils.DateUtil;
import com.namo.spring.db.mysql.domains.diary.entity.ActivityImg;
import com.namo.spring.db.mysql.domains.diary.entity.Diary;
import com.namo.spring.db.mysql.domains.schedule.entity.MeetingSchedule;

public class MeetingDiaryResponseConverter {
	private MeetingDiaryResponseConverter() {
		throw new IllegalStateException("Utill Classes");
	}

	public static MeetingDiaryResponse.DiaryDetailDto toDiaryDetailDto(
		MeetingSchedule meetingSchedule,
		Diary diary,
		List<ActivityImg> activityImgs
	) {
		List<String> urls = activityImgs.stream().map(ActivityImg::getImgUrl).toList();

		return MeetingDiaryResponse.DiaryDetailDto.builder()
			.scheduleId(meetingSchedule.getId())
			.name(meetingSchedule.getCustomTitle())
			.startDate(DateUtil.toSeconds(meetingSchedule.getSchedule().getPeriod().getStartDate()))
			.contents(diary.getMemo())
			.urls(urls)
			.categoryId(meetingSchedule.getCategory().getId())
			.color((long)meetingSchedule.getCategory().getPalette().getColor())
			.placeName(meetingSchedule.getSchedule().getLocation().getName())
			.build();
	}
}
