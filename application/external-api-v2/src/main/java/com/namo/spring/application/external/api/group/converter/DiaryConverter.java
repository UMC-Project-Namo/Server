package com.namo.spring.application.external.api.group.converter;

import com.namo.spring.db.mysql.domains.diary.entity.Diary;
import com.namo.spring.db.mysql.domains.schedule.entity.MeetingSchedule;

public class DiaryConverter {
	private DiaryConverter() {
		throw new IllegalStateException("Util Class");
	}

	public static Diary toDiary(MeetingSchedule meetingSchedule, String memo) {
		return Diary.builder()
			.meetingSchedule(meetingSchedule)
			.memo(memo)
			.build();
	}

}
