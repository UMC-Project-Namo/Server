package com.namo.spring.db.mysql.domains.schedule.repository;

import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findSchedulesByIdIn(List<Long> id);
}
