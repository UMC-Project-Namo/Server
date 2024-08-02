package com.namo.spring.db.mysql.domains.record.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.record.entity.ActivityImage;

public interface ActivityImageRepository extends JpaRepository<ActivityImage, Long> {
}
