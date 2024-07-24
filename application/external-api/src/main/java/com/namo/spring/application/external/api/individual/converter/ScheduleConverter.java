package com.namo.spring.application.external.api.individual.converter;

import com.namo.spring.application.external.api.individual.dto.ScheduleRequest;
import com.namo.spring.db.mysql.domains.individual.domain.Category;
import com.namo.spring.db.mysql.domains.individual.domain.Schedule;
import com.namo.spring.db.mysql.domains.individual.type.Period;
import com.namo.spring.db.mysql.domains.user.domain.User;

public class ScheduleConverter {
	public static Period toPeriod(ScheduleRequest.PostScheduleDto dto) {
		return Period.builder()
			.startDate(dto.getStartDate())
			.endDate(dto.getEndDate())
			.dayInterval(dto.getInterval())
			.build();
	}

	public static Schedule toSchedule(ScheduleRequest.PostScheduleDto dto, Period period, User user,
		Category category) {
		return Schedule.builder()
			.name(dto.getName())
			.period(period)
			.x(dto.getX())
			.y(dto.getY())
			.locationName(dto.getLocationName())
			.kakaoLocationId(dto.getKakaoLocationId())
			.user(user)
			.category(category)
			.build();
	}
}
