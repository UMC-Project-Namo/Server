package com.namo.spring.db.mysql.domains.record.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.record.entity.ActivityParticipants;

public interface ActivityParticipantsRepository extends JpaRepository<ActivityParticipants, Long> {
}