package com.example.namo2.domain.individual.dao.repository.alarm;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.namo2.domain.individual.domain.Alarm;
import com.example.namo2.domain.individual.domain.Schedule;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
	void deleteAllBySchedule(Schedule schedule);
}
