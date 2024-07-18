package com.namo.spring.db.mysql.domains.diary.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.diary.entity.Diary;
import com.namo.spring.db.mysql.domains.schedule.entity.MeetingSchedule;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

	Optional<Diary> findByMeetingSchedule(MeetingSchedule meetingSchedule);

	void deleteByMeetingScheduleId(Long meetingScheduleId);
}
