package com.namo.spring.db.mysql.domains.schedule.repository;

import com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    @Query("SELECT p FROM Participant p JOIN p.schedule s JOIN p.member m WHERE p.member = :member AND p.status = 'ACTIVE' AND s.scheduleType = :scheduleType")
    List<Participant> findParticipantsByMemberAndScheduleType(Member member, int scheduleType);

    @Query("SELECT COUNT(p) > 0 FROM Participant p WHERE p.schedule.id = :scheduleId AND p.member.id = :memberId AND p.isOwner = 1")
    boolean existsParticipantByScheduleIdAndMemberId(Long scheduleId, Long memberId);

    @Query("SELECT DISTINCT new com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery(" +
            "p.id, m.id, m.nickname, s" +
            ")FROM Participant p " +
            "JOIN p.schedule s " +
            "JOIN p.member m " +
            "WHERE m.id IN :memberIds " +
            "AND p.status = 'ACTIVE' " +
            "AND (s.period.startDate <= :endDate " +
            "AND s.period.endDate >= :startDate) " +
            "ORDER BY s.period.startDate ASC")
    List<ScheduleParticipantQuery> findParticipantsWithScheduleAndMember(List<Long> memberIds, LocalDateTime startDate, LocalDateTime endDate);
}
