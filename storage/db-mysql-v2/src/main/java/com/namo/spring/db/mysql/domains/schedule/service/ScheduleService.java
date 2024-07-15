package com.namo.spring.db.mysql.domains.schedule.service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class ScheduleService {
	private final ScheduleRepository scheduleRepository;

	@Transactional
	public Schedule createSchedule(Schedule schedule) {
		return scheduleRepository.save(schedule);
	}

	@Transactional(readOnly = true)
	public Optional<Schedule> readSchedule(Long id) {
		return scheduleRepository.findById(id);
	}

	@Transactional
	public void deleteSchedule(Long id) {
		scheduleRepository.deleteById(id);
	}

}
