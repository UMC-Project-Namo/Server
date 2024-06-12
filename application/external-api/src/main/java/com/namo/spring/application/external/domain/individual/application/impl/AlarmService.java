package com.namo.spring.application.external.domain.individual.application.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.namo.spring.application.external.domain.individual.domain.Alarm;
import com.namo.spring.application.external.domain.individual.domain.Schedule;
import com.namo.spring.application.external.domain.individual.repository.alarm.AlarmRepository;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.IndividualException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlarmService {
	private final AlarmRepository alarmRepository;

	public void removeAlarmsBySchedule(Schedule schedule) {
		alarmRepository.deleteAllBySchedule(schedule);
	}

	public void removeAlarmsBySchedules(List<Schedule> schedules) {
		schedules.forEach(schedule ->
			alarmRepository.deleteAll(schedule.getAlarms())
		);
	}

	public void checkValidAlarm(List<Alarm> alarms) {
		alarms.forEach(alarm -> {
				Integer time = alarm.getAlarmDate();
				if (time != 0 &&
					time != 5 &&
					time != 10 &&
					time != 30 &&
					time != 60)
					throw new IndividualException(ErrorStatus.INVALID_ALARM);
			}
		);
	}
}
