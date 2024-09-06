package com.namo.spring.application.external.api.schedule.converter;

import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
import com.namo.spring.application.external.api.schedule.dto.ScheduleResponse;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.type.Period;

public class ScheduleConverter {
	private ScheduleConverter() {
		throw new IllegalStateException("Util Class");
	}

	public static Schedule toSchedule(String title, Period period, ScheduleRequest.LocationDto location, int type,
		String imageUrl, Integer participantCount, String participantNames) {
		return Schedule.builder()
			.title(title)
			.period(period)
			.location(location != null ? LocationConverter.toLocation(location) : null)
			.scheduleType(type)
			.imageUrl(imageUrl)
			.participantCount(participantCount)
			.participantNicknames(participantNames)
			.build();
	}

	public static ScheduleResponse.ScheduleSummaryDto toScheduleSummaryDto(Schedule schedule) {
		return ScheduleResponse.ScheduleSummaryDto.builder()
			.scheduleId(schedule.getId())
			.scheduleTitle(schedule.getTitle())
			.scheduleStartDate(schedule.getPeriod().getStartDate())
			.location(LocationConverter.toLocationInfoDto(schedule.getLocation()))
			.build();
	}

}
