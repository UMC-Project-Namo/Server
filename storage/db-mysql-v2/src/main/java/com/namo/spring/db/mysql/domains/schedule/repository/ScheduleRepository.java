package com.namo.spring.db.mysql.domains.schedule.repository;

import com.namo.spring.db.mysql.domains.schedule.dto.MeetingScheduleQueryDto;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT NEW com.namo.spring.db.mysql.domains.schedule.dto.MeetingScheduleQueryDto(s.id, s.title, p, COUNT(p)) FROM Schedule s JOIN s.participantList p WHERE p.member = :member AND s.scheduleType = 1 AND p.status = 'ACTIVE' ORDER BY s.period.startDate")
    List<MeetingScheduleQueryDto> findMeetingSchedulesWithParticipantsByMember(Member member);
}
