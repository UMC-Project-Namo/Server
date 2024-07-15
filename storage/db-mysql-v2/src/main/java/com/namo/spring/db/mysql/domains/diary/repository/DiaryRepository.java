package com.namo.spring.db.mysql.domains.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.diary.entity.Diary;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
