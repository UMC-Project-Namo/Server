package com.namo.spring.db.mysql.domains.individual.repository.alarm;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.individual.domain.Alarm;
import com.namo.spring.db.mysql.domains.individual.domain.Schedule;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
	void deleteAllBySchedule(Schedule schedule);
}
