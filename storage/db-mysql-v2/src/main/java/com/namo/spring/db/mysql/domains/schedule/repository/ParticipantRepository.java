package com.namo.spring.db.mysql.domains.schedule.repository;

import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    @Query("SELECT p FROM Participant p JOIN FETCH p.schedule s JOIN FETCH p.member m WHERE p.member = :member AND p.status = 'ACTIVE' AND s.scheduleType = :scheduleType")
    List<Participant> findParticipantsByMemberAndScheduleType(Member member, int scheduleType);

    @Query("select p FROM Participant p JOIN FETCH p.schedule s LEFT JOIN FETCH p.member AND LEFT JOIN FETCH p.anonymous WHERE s.id in :ids AND p.status = 'ACTIVE' ORDER BY p.schedule.period.startDate ASC")
    List<Participant> findParticipantsWithSchedulesByScheduleIds(List<Long> ids);

}
