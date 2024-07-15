package com.namo.spring.db.mysql.domains.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.diary.entity.ActivityImg;

public interface ActivityImgRepository extends JpaRepository<ActivityImg, Long> {
}
