package com.namo.spring.db.mysql.domains.schedule.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.namo.spring.db.mysql.domains.schedule.model.query.ScheduleSummaryQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.schedule.model.query.ScheduleParticipantQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findAllByScheduleId(Long scheduleId);

    @Query("SELECT new com.namo.spring.db.mysql.domains.schedule.model.query.ScheduleSummaryQuery(" +
            "s.id, " +
            "p.customTitle, " +
            "s.period.startDate, " +
            "s.period.endDate, " +
            "p.customImage, " +
            "s.participantCount, " +
            "s.participantNicknames, " +
            "p.hasDiary, " +
            " (SELECT a.id FROM Activity a WHERE a.schedule = s ORDER BY a.createdAt DESC LIMIT 1)) " +
            "FROM Participant p " +
            "LEFT JOIN Schedule s ON p.schedule = s " +
            "LEFT JOIN Member m ON p.member = m " +
            "WHERE m.id = :memberId " +
            "AND s.scheduleType = :scheduleType")
    List<ScheduleSummaryQuery> findScheduleSummaryByMemberAndScheduleType(Long memberId, int scheduleType);

    boolean existsByScheduleIdAndMemberId(Long scheduleId, Long memberId);

    boolean existsByScheduleIdAndAnonymousId(Long scheduleId, Long anonymousId);

    @Query("SELECT p FROM Participant p JOIN FETCH p.schedule s LEFT JOIN FETCH p.palette " +
        "WHERE s.id = :scheduleId AND s.scheduleType = :scheduleType ")
    List<Participant> findParticipantsByScheduleIdAndStatusAndType(Long scheduleId, int scheduleType);

    @Query("SELECT p FROM Participant p JOIN FETCH p.member m JOIN FETCH p.schedule s WHERE s.id = :scheduleId AND m.id = :memberId")
    Optional<Participant> findParticipantByScheduleIdAndMemberId(Long scheduleId, Long memberId);

    @Query("SELECT p " +
            "FROM Participant p " +
            "JOIN p.schedule s " +
            "LEFT JOIN FETCH p.member m " +
            "LEFT JOIN FETCH p.anonymous a " +
            "WHERE p.id in :ids AND s.id = :scheduleId")
    List<Participant> findParticipantByIdAndScheduleIdFetchUser(List<Long> ids, Long scheduleId);

    @Query("SELECT p " +
            "FROM Participant p " +
            "JOIN p.schedule s " +
            "WHERE p.id in :ids AND s.id = :scheduleId")
    List<Participant> findParticipantByIdAndScheduleId(List<Long> ids, Long scheduleId);

    @Query("SELECT DISTINCT new com.namo.spring.db.mysql.domains.schedule.model.query.ScheduleParticipantQuery(" +
            "p.id, m.palette.id, m.id, m.nickname, s, p.customTitle, p.customImage, p.category.isShared, m.birthdayVisible" +
            ") FROM Participant p " +
            "JOIN p.schedule s " +
            "JOIN p.member m " +
            "WHERE m.id IN :memberIds " +
            "AND (s.period.startDate < :endDate " +
            "AND s.period.endDate >= :startDate) " +
            "ORDER BY s.period.startDate ASC")
    List<ScheduleParticipantQuery> findParticipantsWithUserAndScheduleByPeriod(
            List<Long> memberIds,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    @Query("SELECT p FROM Participant p " +
            "JOIN FETCH p.schedule s " +
            "JOIN FETCH p.category c " +
            "WHERE p.member.id = :memberId " +
            "AND (s.period.startDate < :endDate " +
            "AND s.period.endDate >= :startDate) " +
            "AND (:isShared IS NULL OR c.isShared = :isShared) " +
            "ORDER BY s.period.startDate ASC")
    List<Participant> findParticipantsWithScheduleAndCategoryByPeriod(
            Long memberId,
            Boolean isShared,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    void deleteByIdIn(List<Long> id);

    Optional<Participant> findParticipantByMemberIdAndScheduleId(Long memberId, Long scheduleId);

    Optional<Participant> findParticipantByAnonymousIdAndScheduleId(Long anonymousId, Long scheduleId);

    @Query("SELECT p "
            + "FROM Participant p "
            + "JOIN FETCH p.diary d "
            + "WHERE p.hasDiary = true and p.member.id = :memberId "
            + "order by p.schedule.period.startDate desc ")
    List<Participant> findAllByMemberIdAndHasDiary(@Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT p "
            + "FROM Participant p "
            + "JOIN FETCH p.diary d "
            + "WHERE p.hasDiary = true AND p.member.id = :memberId AND p.schedule.title LIKE %:keyword% "
            + "ORDER BY p.schedule.period.startDate DESC")
    List<Participant> findAllByScheduleTitleAndHasDiary(@Param("memberId") Long memberId,
            @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p "
            + "FROM Participant p "
            + "JOIN FETCH p.diary d "
            + "WHERE p.hasDiary = true AND p.member.id = :memberId AND p.diary.content LIKE %:keyword% "
            + "ORDER BY p.schedule.period.startDate DESC")
    List<Participant> findAllByDiaryContentAndHasDiary(@Param("memberId") Long memberId,
            @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p "
            + "FROM Participant p "
            + "JOIN FETCH p.diary d "
            + "WHERE p.hasDiary = true AND p.member.id = :memberId AND p.schedule.participantNicknames LIKE %:keyword% "
            + "ORDER BY p.schedule.period.startDate DESC")
    List<Participant> findAllByMemberAndHasDiary(@Param("memberId") Long memberId, @Param("keyword") String keyword,
            Pageable pageable);

    @Query("SELECT p "
            + "FROM Participant p "
            + "JOIN FETCH p.diary d "
            + "WHERE p.hasDiary = true "
            + "AND p.member.id = :memberId "
            + "AND p.schedule.period.startDate BETWEEN :startDate AND :endDate")
    List<Participant> findAllByDateRangeAndHasDiary(@Param("memberId") Long memberId,
            @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    boolean existsByCategory(Category category);

    @Query("SELECT p "+
            "FROM Participant p " +
            "JOIN FETCH p.member m "+
            "WHERE p.schedule.id = :scheduleId " +
            "AND p.isOwner = 0 " +
            "ORDER BY m.nickname " +
            "LIMIT 1")
    Optional<Participant> findFirstParticipantByScheduleIdOrderByNickname(Long scheduleId);
}

