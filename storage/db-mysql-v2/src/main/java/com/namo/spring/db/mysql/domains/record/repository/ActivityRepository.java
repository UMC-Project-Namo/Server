package com.namo.spring.db.mysql.domains.record.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.record.entity.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

}
