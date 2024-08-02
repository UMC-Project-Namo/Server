package com.namo.spring.db.mysql.domains.record.service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.record.entity.ActivityParticipants;
import com.namo.spring.db.mysql.domains.record.repository.ActivityParticipantsRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class ActivityUserService {
	private final ActivityParticipantsRepository activityUserRepository;

	@Transactional
	public ActivityParticipants createActivityParticipants(ActivityParticipants activityParticipants) {
		return activityUserRepository.save(activityParticipants);
	}

	@Transactional(readOnly = true)
	public Optional<ActivityParticipants> readActivityParticipants(Long activityParticipantsId) {
		return activityUserRepository.findById(activityParticipantsId);
	}

	@Transactional
	public void deleteActivityParticipants(Long activityParticipantsId) {
		activityUserRepository.deleteById(activityParticipantsId);
	}
}
