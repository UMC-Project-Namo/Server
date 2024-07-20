package com.namo.spring.application.external.api.group.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional(readOnly = true)
	public MeetingSchedule readMeetingSchedule(Long meetingScheduleId) {
		return meetingScheduleService.readMeetingSchedule(meetingScheduleId).orElseThrow(
			() -> new MeetingScheduleException(ErrorStatus.NOT_FOUND_MEETING_SCHEDULE_FAILURE)
		);
	}

	@Transactional(readOnly = true)
	public Page<MeetingSchedule> readAllByMonth(Integer year, Integer month, Pageable pageable) {
		return meetingScheduleService.readAllByMonth(year, month, pageable);
	}
}
