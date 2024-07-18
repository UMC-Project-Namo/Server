package com.namo.spring.db.mysql.domains.schedule.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.namo.spring.db.mysql.domains.schedule.entity.MeetingSchedule;

public interface MeetingScheduleRepository extends JpaRepository<MeetingSchedule, Long> {

	@Query("SELECT ms from MeetingSchedule ms "
		+ "WHERE YEAR(ms.schedule.period.startDate) = :year AND MONTH(ms.schedule.period.startDate) = :month")
	Page<MeetingSchedule> findAllByMonth(@Param("year") Integer year, @Param("month") Integer month, Pageable pageable);
}
