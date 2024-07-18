package com.namo.spring.db.mysql.domains.diary.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.diary.entity.ActivityImg;
import com.namo.spring.db.mysql.domains.diary.entity.Diary;

public interface ActivityImgRepository extends JpaRepository<ActivityImg, Long> {
	Optional<List<ActivityImg>> findAllByDiary(Diary diary);
}
