package com.namo.spring.db.mysql.domains.schedule.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.type.ParticipantStatus;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

	List<Participant> findAllByScheduleId(Long scheduleId);

	@Query("SELECT p FROM Participant p JOIN p.schedule s JOIN p.member m WHERE p.member.id = :memberId AND p.status = 'ACTIVE' AND s.scheduleType = :scheduleType")
	List<Participant> findParticipantsByMemberAndScheduleType(Long memberId, int scheduleType);

	boolean existsByScheduleIdAndMemberId(Long scheduleId, Long memberId);

	@Query("SELECT p FROM Participant p JOIN FETCH p.schedule s LEFT JOIN FETCH p.palette " +
		"WHERE s.id = :scheduleId AND s.scheduleType = :scheduleType " +
		"AND (:status IS NULL OR p.status = :status)")
	List<Participant> findParticipantsByScheduleIdAndStatusAndType(Long scheduleId, int scheduleType,
		ParticipantStatus status);

	@Query("SELECT p FROM Participant p JOIN FETCH p.schedule s WHERE s.id = :scheduleId AND p.member.id = :memberId")
	Optional<Participant> findParticipantByScheduleIdAndMemberId(Long scheduleId, Long memberId);

	@Query("SELECT p " +
		"FROM Participant p " +
		"JOIN p.schedule s " +
		"LEFT JOIN FETCH p.member m " +
		"LEFT JOIN FETCH p.anonymous a " +
		"WHERE p.id in :ids AND s.id = :scheduleId AND p.status = :status")
	List<Participant> findParticipantByIdAndScheduleId(List<Long> ids, Long scheduleId, ParticipantStatus status);

	@Query("SELECT DISTINCT new com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery(" +
		"p.id, p.palette.id, m.id, m.nickname, s" +
		") FROM Participant p " +
		"JOIN p.schedule s " +
		"LEFT JOIN p.member m " +
		"WHERE m.id IN :memberIds " +
		"AND p.status = 'ACTIVE' " +
		"AND (s.period.startDate <= :endDate " +
		"AND s.period.endDate >= :startDate) " +
		"ORDER BY s.period.startDate ASC")
	List<ScheduleParticipantQuery> findParticipantsWithUserAndSchedule(
		List<Long> memberIds,
		LocalDateTime startDate,
		LocalDateTime endDate
	);

	void deleteByIdIn(List<Long> id);

	Optional<Participant> findParticipantByMemberIdAndScheduleId(Long memberId, Long scheduleId);

	@Query("SELECT p "
		+ "FROM Participant p "
		+ "WHERE p.hasDiary == true and p.member.id == :memberId "
		+ "order by p.schedule.period.startDate desc ")
	List<Participant> findAllByMemberIdAndHasDiary(Long memberId, Pageable pageable);

	@Query("SELECT p "
		+ "FROM Participant p "
		+ "WHERE p.hasDiary = true AND p.member.id = :memberId AND p.schedule.title LIKE %:keyword% "
		+ "ORDER BY p.schedule.period.startDate DESC")
	List<Participant> findAllByScheduleTitleAndHasDiary(Long memberId, String keyword, Pageable pageable);

}
