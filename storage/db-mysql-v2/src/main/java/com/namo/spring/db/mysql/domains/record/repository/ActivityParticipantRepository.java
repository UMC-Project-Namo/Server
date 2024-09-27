package com.namo.spring.db.mysql.domains.record.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.record.entity.Activity;
import com.namo.spring.db.mysql.domains.record.entity.ActivityParticipant;

public interface ActivityParticipantRepository extends JpaRepository<ActivityParticipant, Long> {
    List<ActivityParticipant> findByActivityAndIncludedInSettlementAndParticipantIdIn(Activity activity, boolean isInSettlement, List<Long> participantIdList);
}
