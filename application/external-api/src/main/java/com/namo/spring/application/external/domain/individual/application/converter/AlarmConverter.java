package com.namo.spring.application.external.domain.individual.application.converter;

import java.util.List;

import com.namo.spring.application.external.domain.individual.domain.Alarm;
import com.namo.spring.application.external.domain.individual.domain.Schedule;
import com.namo.spring.application.external.domain.individual.ui.dto.ScheduleRequest;

public class AlarmConverter {

	public static List<Alarm> toAlarms(ScheduleRequest.PostScheduleDto req, Schedule schedule) {
		return req.getAlarmDate().stream().map(alarm ->
			Alarm.builder()
				.alarmDate(alarm)
				.schedule(schedule)
				.build()
		).toList();
	}
}
