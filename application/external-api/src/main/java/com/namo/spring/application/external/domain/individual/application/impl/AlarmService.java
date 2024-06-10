package com.example.namo2.domain.individual.application.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.namo2.domain.individual.dao.repository.alarm.AlarmRepository;
import com.example.namo2.domain.individual.domain.Alarm;
import com.example.namo2.domain.individual.domain.Schedule;

import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;

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
					throw new BaseException(BaseResponseStatus.INVALID_ALARM);
			}
		);
	}
}
