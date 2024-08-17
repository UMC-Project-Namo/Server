package com.namo.spring.db.mysql.domains.schedule.repository;

import com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantItemQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    @Query("SELECT p FROM Participant p JOIN p.schedule s JOIN p.member m WHERE p.member = :member AND p.status = 'ACTIVE' AND s.scheduleType = :scheduleType")
    List<Participant> findParticipantsByMemberAndScheduleType(Member member, int scheduleType);

    @Query("select new com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantItemQuery(s.id, s.title, s.period.startDate, s.imageUrl, " +
            "CASE WHEN p.member IS NOT NULL THEN p.member.name ELSE p.anonymous.name END) " +
            "FROM Participant p JOIN p.schedule s LEFT JOIN p.member " +
            "LEFT JOIN p.anonymous WHERE s.id in :ids AND p.status = 'ACTIVE' ORDER BY p.schedule.period.startDate ASC")
    List<ScheduleParticipantItemQuery> findScheduleParticipantItemsByIds(List<Long> ids);

    @Query("SELECT COUNT(p) > 0 FROM Participant p WHERE p.schedule.id = :scheduleId AND p.member.id = :memberId AND p.isOwner = 1")
    boolean existsParticipantByScheduleIdAndMemberId(Long scheduleId, Long memberId);
}
