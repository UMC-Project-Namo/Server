package com.namo.spring.application.external.api.group.service;

import org.springframework.stereotype.Service;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.schedule.entity.MeetingSchedule;
import com.namo.spring.db.mysql.domains.schedule.exception.MeetingScheduleException;
import com.namo.spring.db.mysql.domains.schedule.service.MeetingScheduleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingScheduleSearchService {
	private final MeetingScheduleService meetingScheduleService;

	public MeetingSchedule readMeetingSchedule(Long meetingScheduleId) {
		return meetingScheduleService.readMeetingSchedule(meetingScheduleId).orElseThrow(
			() -> new MeetingScheduleException(ErrorStatus.NOT_FOUND_MEETING_SCHEDULE_FAILURE)
		);
	}
}
