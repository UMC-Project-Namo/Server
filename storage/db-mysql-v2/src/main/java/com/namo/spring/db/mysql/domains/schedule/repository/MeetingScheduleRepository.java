package com.namo.spring.db.mysql.domains.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.schedule.entity.MeetingSchedule;

public interface MeetingScheduleRepository extends JpaRepository<MeetingSchedule, Long> {
}
