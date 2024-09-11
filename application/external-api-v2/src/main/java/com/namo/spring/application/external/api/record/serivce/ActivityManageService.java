package com.namo.spring.application.external.api.record.serivce;

import org.springframework.stereotype.Component;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.record.entity.Activity;
import com.namo.spring.db.mysql.domains.record.exception.ActivityException;
import com.namo.spring.db.mysql.domains.record.service.ActivityService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ActivityManageService {

	private final ActivityService activityService;

	/**
	 * 활동을 찾는 메서드입니다.
	 * !! 활동이 존재 하는지 검증합니다.
	 *
	 * @param memberId
	 * @param activityId
	 * @return
	 */
	public Activity getMyActivity(Long memberId, Long activityId) {
		return activityService.readActivity(activityId)
			.orElseThrow(() -> new ActivityException(ErrorStatus.NOT_FOUND_GROUP_ACTIVITY_FAILURE));
	}
}
