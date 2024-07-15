package com.namo.spring.db.mysql.domains.diary.service;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.diary.entity.Activity;
import com.namo.spring.db.mysql.domains.diary.repository.ActivityRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class ActivityService {
	private final ActivityRepository activityRepository;

	@Transactional
	public Activity createActivity(Activity activity) {
		return activityRepository.save(activity);
	}

	@Transactional(readOnly = true)
	public Optional<Activity> readActivity(Long activityId) {
		return activityRepository.findById(activityId);
	}

	@Transactional(readOnly = true)
	public List<Activity> raedActivityListByMeetingScheduleId(Long meetingScheduleId) {
		return activityRepository.findByMeetingScheduleId(meetingScheduleId);
	}

	@Transactional
	public void deleteActivity(Long activityId) {
		activityRepository.deleteById(activityId);
	}
}
