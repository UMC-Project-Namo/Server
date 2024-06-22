package com.namo.spring.application.external.api.group.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.GroupException;
import com.namo.spring.db.mysql.domains.group.domain.MoimSchedule;
import com.namo.spring.db.mysql.domains.group.domain.MoimScheduleAndUser;
import com.namo.spring.db.mysql.domains.group.repository.schedule.MoimScheduleAndUserRepository;
import com.namo.spring.db.mysql.domains.group.repository.schedule.MoimScheduleRepository;
import com.namo.spring.db.mysql.domains.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoimScheduleService {
	private final MoimScheduleRepository moimScheduleRepository;
	private final MoimScheduleAndUserRepository moimScheduleAndUserRepository;

	/**
	 * TODO: MoimSchedule 생성시 밸리데이션 처리
	 * 자신이 모임에 소속된 사람이 아닐 시 모임에 대한 스케줄을 생성할 수 없게
	 * 검증 처리가 있으면 좋을 듯합니다.
	 */
	public MoimSchedule createMoimSchedule(MoimSchedule moimSchedule) {
		return moimScheduleRepository.save(moimSchedule);
	}

	public MoimSchedule getMoimSchedule(Long id) {
		return moimScheduleRepository.findById(id)
			.orElseThrow(() -> new GroupException(ErrorStatus.NOT_FOUND_SCHEDULE_FAILURE));
	}

	public MoimSchedule getMoimScheduleWithMoimMemo(Long id) {
		return moimScheduleRepository.findMoimScheduleWithMoimMemoById(id)
			.orElseThrow(() -> new GroupException(ErrorStatus.NOT_FOUND_SCHEDULE_FAILURE));
	}

	public MoimSchedule getMoimScheduleWithMoimScheduleAndUsers(Long id) {
		return moimScheduleRepository.findMoimSheduleAndMoimScheduleAndUsersById(id)
			.orElseThrow(() -> new GroupException(ErrorStatus.NOT_FOUND_SCHEDULE_FAILURE));
	}

	public void removeMoimSchedule(MoimSchedule moimSchedule) {
		moimScheduleRepository.delete(moimSchedule);
	}

	public List<MoimScheduleAndUser> getMonthMoimSchedules(
		List<LocalDateTime> localDateTimes, List<User> users) {
		return moimScheduleAndUserRepository
			.findMoimScheduleAndUserWithMoimScheduleByUsersAndDates(
				localDateTimes.get(0), localDateTimes.get(1), users
			);
	}

	public List<MoimScheduleAndUser> getAllMoimSchedules(List<User> users) {
		return moimScheduleAndUserRepository
			.findMoimScheduleAndUserWithMoimScheduleByUsersAndDates(
				null, null, users
			);
	}

	public List<MoimScheduleAndUser> getMoimScheduleAndUsers(MoimSchedule moimSchedule) {
		return moimScheduleAndUserRepository.findMoimScheduleAndUserByMoimSchedule(moimSchedule);
	}
}
