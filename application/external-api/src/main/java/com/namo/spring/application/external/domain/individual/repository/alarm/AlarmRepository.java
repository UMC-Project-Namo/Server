package com.namo.spring.application.external.domain.individual.repository.alarm;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.application.external.domain.individual.domain.Alarm;
import com.namo.spring.application.external.domain.individual.domain.Schedule;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
	void deleteAllBySchedule(Schedule schedule);
}
