package com.example.namo2.domain.individual.application.converter;

import com.example.namo2.domain.individual.domain.Category;

import com.example.namo2.domain.individual.domain.constant.Period;
import com.example.namo2.domain.individual.domain.Schedule;
import com.example.namo2.domain.individual.ui.dto.ScheduleRequest;

import com.example.namo2.domain.user.domain.User;

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
