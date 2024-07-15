package com.namo.spring.db.mysql.domains.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.diary.entity.DiaryImg;

public interface DiaryImgRepository extends JpaRepository<DiaryImg, Long> {
}
