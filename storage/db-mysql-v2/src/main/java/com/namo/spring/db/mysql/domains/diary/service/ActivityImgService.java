package com.namo.spring.db.mysql.domains.diary.service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.diary.entity.ActivityImg;
import com.namo.spring.db.mysql.domains.diary.repository.ActivityImgRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class ActivityImgService {
	private final ActivityImgRepository activityImgRepository;

	@Transactional
	public ActivityImg createActivityImg(ActivityImg activityImg) {
		return activityImgRepository.save(activityImg);
	}

	@Transactional(readOnly = true)
	public Optional<ActivityImg> readActivityImg(Long activityImgId) {
		return activityImgRepository.findById(activityImgId);
	}

	@Transactional
	public void deleteActivityImg(Long activityImgId) {
		activityImgRepository.deleteById(activityImgId);
	}
}
