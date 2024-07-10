package com.namo.spring.application.external.api.individual.converter;

import java.util.List;

import com.namo.spring.db.mysql.domains.individual.domain.Alarm;
import com.namo.spring.db.mysql.domains.individual.domain.Schedule;
import com.namo.spring.application.external.api.individual.dto.ScheduleRequest;

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
