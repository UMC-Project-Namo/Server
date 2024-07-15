package com.namo.spring.db.mysql.domains.diary.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.namo.spring.db.mysql.domains.diary.entity.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

	@Query("SELECT a FROM Activity a WHERE a.meetingSchedule.id = :meetingScheduleId")
	List<Activity> findByMeetingScheduleId(Long meetingScheduleId);
}
