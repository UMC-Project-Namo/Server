package com.namo.spring.db.mysql.domains.record.repository;

import com.namo.spring.db.mysql.domains.record.entity.ActivityParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityParticipantRepository extends JpaRepository<ActivityParticipant, Long> {
}
