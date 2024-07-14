package com.namo.spring.db.mysql.domains.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.schedule.entity.PersonalSchedule;

public interface PersonalScheduleRepository extends JpaRepository<PersonalSchedule, Long> {
}
