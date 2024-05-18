package com.example.namo2.domain.individual.application.impl;

import static com.example.namo2.global.common.response.BaseResponseStatus.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.namo2.domain.individual.dao.repository.schedule.ScheduleRepository;
import com.example.namo2.domain.individual.domain.Schedule;
import com.example.namo2.domain.individual.ui.dto.DiaryResponse;
import com.example.namo2.domain.individual.ui.dto.ScheduleResponse;

import com.example.namo2.domain.user.domain.User;

import com.example.namo2.global.common.exception.BaseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	private final ScheduleRepository scheduleRepository;

	public Schedule createSchedule(Schedule schedule) {
		return scheduleRepository.save(schedule);
	}

	public Schedule getScheduleById(Long scheduleId) {
		return scheduleRepository.findById(scheduleId).orElseThrow(() -> new BaseException(NOT_FOUND_SCHEDULE_FAILURE));
	}

	public List<Schedule> getSchedulesByUser(User user) {
		return scheduleRepository.findAllByUser(user);
	}

	public List<ScheduleResponse.GetScheduleDto> getSchedulesByUserId(User user, LocalDateTime startDate,
		LocalDateTime endDate
	) {
		return scheduleRepository.findSchedulesByUserId(user, startDate, endDate);
	}

	public List<ScheduleResponse.GetScheduleDto> getMoimSchedulesByUser(User user, LocalDateTime startDate,
		LocalDateTime endDate
	) {
		return scheduleRepository.findMoimSchedulesByUserId(user, startDate, endDate);
	}

	public List<ScheduleResponse.GetScheduleDto> getAllSchedulesByUser(User user) {
		return scheduleRepository.findSchedulesByUserId(user, null, null);
	}

	public List<ScheduleResponse.GetScheduleDto> getAllMoimSchedulesByUser(User user) {
		return scheduleRepository.findMoimSchedulesByUserId(user, null, null);
	}

	public DiaryResponse.SliceDiaryDto getScheduleDiaryByUser(
		User user,
		LocalDateTime startDate,
		LocalDateTime endDate,
		Pageable pageable
	) {
		return scheduleRepository.findScheduleDiaryByMonthDto(user, startDate, endDate, pageable);
	}

	public List<DiaryResponse.GetDiaryByUserDto> getAllDiariesByUser(User user) {
		return scheduleRepository.findAllScheduleDiary(user);
	}

	public void removeSchedule(Schedule schedule) {
		scheduleRepository.delete(schedule);
	}

	public void removeSchedules(List<Schedule> schedules) {
		scheduleRepository.deleteAll(schedules);
	}

	public List<Schedule> getSchedules(List<User> users) {
		return scheduleRepository.findSchedulesByUsers(users);
	}
}
