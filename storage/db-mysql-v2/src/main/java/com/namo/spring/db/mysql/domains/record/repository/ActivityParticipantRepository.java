package com.namo.spring.db.mysql.domains.record.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.namo.spring.db.mysql.domains.record.entity.Activity;
import com.namo.spring.db.mysql.domains.record.entity.ActivityParticipant;

public interface ActivityParticipantRepository extends JpaRepository<ActivityParticipant, Long> {

    @Query("SELECT ap FROM ActivityParticipant ap WHERE ap.activity = :activity AND ap.isIncludedInSettlement = :includedInSettlement AND ap.participant.id IN :participantIdList")
    List<ActivityParticipant> findByActivityAndIncludedInSettlementAndParticipantIdIn(
            @Param("activity") Activity activity,
            @Param("includedInSettlement") boolean includedInSettlement,
            @Param("participantIdList") List<Long> participantIdList);

}
