package com.namo.spring.db.mysql.domains.record.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.record.entity.Diary;
import com.namo.spring.db.mysql.domains.record.entity.DiaryImage;

public interface DiaryImageRepository extends JpaRepository<DiaryImage, Long> {
	void deleteAllByDiary(Diary diary);
}
