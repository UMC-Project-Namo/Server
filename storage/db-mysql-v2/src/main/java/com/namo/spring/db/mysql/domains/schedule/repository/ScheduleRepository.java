package com.namo.spring.db.mysql.domains.schedule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findSchedulesByIdIn(List<Long> id);
}
