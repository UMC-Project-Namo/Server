package com.example.namo2.domain.individual.application.converter;

import java.util.List;

import com.example.namo2.domain.individual.domain.Alarm;
import com.example.namo2.domain.individual.domain.Schedule;
import com.example.namo2.domain.individual.ui.dto.ScheduleRequest;

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
