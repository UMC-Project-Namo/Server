package com.namo.spring.db.mysql.domains.schedule.service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.schedule.entity.MeetingSchedule;
import com.namo.spring.db.mysql.domains.schedule.repository.MeetingScheduleRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class MeetingScheduleService {
	private final MeetingScheduleRepository meetingScheduleRepository;

	@Transactional
	public MeetingSchedule createMeetingSchedule(MeetingSchedule meetingSchedule) {
		return meetingScheduleRepository.save(meetingSchedule);
	}

	@Transactional(readOnly = true)
	public Optional<MeetingSchedule> getMeetingSchedule(Long id) {
		return meetingScheduleRepository.findById(id);
	}

	@Transactional
	public void deleteMeetingSchedule(Long id) {
		meetingScheduleRepository.deleteById(id);
	}
}
