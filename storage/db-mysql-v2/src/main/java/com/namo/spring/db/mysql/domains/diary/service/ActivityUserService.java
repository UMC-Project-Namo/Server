package com.namo.spring.db.mysql.domains.diary.service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.diary.entity.ActivityUser;
import com.namo.spring.db.mysql.domains.diary.repository.ActivityUserRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class ActivityUserService {
	private final ActivityUserRepository activityUserRepository;

	@Transactional
	public ActivityUser createActivityUser(ActivityUser activityUser) {
		return activityUserRepository.save(activityUser);
	}

	@Transactional(readOnly = true)
	public Optional<ActivityUser> readActivityUser(Long activityUserId) {
		return activityUserRepository.findById(activityUserId);
	}

	@Transactional
	public void deleteActivityUser(Long activityUserId) {
		activityUserRepository.deleteById(activityUserId);
	}
}
