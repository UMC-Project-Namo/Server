package com.namo.spring.db.mysql.domains.schedule.repository;

import com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.type.ParticipantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Query("SELECT p FROM Participant p JOIN p.schedule s JOIN p.member m WHERE p.member.id = :memberId AND p.status = 'ACTIVE' AND s.scheduleType = :scheduleType")
    List<Participant> findParticipantsByMemberAndScheduleType(Long memberId, int scheduleType);

    boolean existsByScheduleIdAndMemberId(Long scheduleId, Long memberId);

    @Query("SELECT p FROM Participant p JOIN FETCH p.schedule s JOIN FETCH p.palette " +
            "WHERE s.id = :scheduleId AND s.scheduleType = :scheduleType " +
            "AND (:status IS NULL OR p.status = :status)")
    List<Participant> findParticipantsByScheduleIdAndScheduleType(Long scheduleId, int scheduleType, ParticipantStatus status);

    @Query("SELECT DISTINCT new com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery(" +
            "p.id, p.palette.id, m.id, null, m.nickname, s" +
            ") FROM Participant p " +
            "JOIN p.schedule s " +
            "JOIN p.member m " +
            "WHERE m.id IN :memberIds " +
            "AND p.status = 'ACTIVE' " +
            "AND (s.period.startDate <= :endDate " +
            "AND s.period.endDate >= :startDate) " +
            "ORDER BY s.period.startDate ASC")
    List<ScheduleParticipantQuery> findParticipantsWithScheduleAndMember(
            List<Long> memberIds,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    @Query("SELECT DISTINCT new com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery(" +
            "p.id, p.palette.id, null, a.id, a.nickname, s" +
            ") FROM Participant p " +
            "JOIN p.schedule s " +
            "JOIN p.anonymous a " +
            "WHERE a.id IN :anonymousIds " +
            "AND p.status = 'ACTIVE' " +
            "AND (s.period.startDate <= :endDate " +
            "AND s.period.endDate >= :startDate) " +
            "ORDER BY s.period.startDate ASC")
    List<ScheduleParticipantQuery> findParticipantsWithScheduleAndAnonymous(
            List<Long> anonymousIds,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
