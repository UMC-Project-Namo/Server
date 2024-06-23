package com.namo.spring.application.external.api.group.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.GroupException;
import com.namo.spring.db.mysql.domains.group.domain.Moim;
import com.namo.spring.db.mysql.domains.group.domain.MoimAndUser;
import com.namo.spring.db.mysql.domains.group.domain.MoimSchedule;
import com.namo.spring.db.mysql.domains.group.domain.MoimScheduleAlarm;
import com.namo.spring.db.mysql.domains.group.domain.MoimScheduleAndUser;
import com.namo.spring.db.mysql.domains.group.repository.schedule.MoimScheduleAlarmRepository;
import com.namo.spring.db.mysql.domains.group.repository.schedule.MoimScheduleAndUserRepository;
import com.namo.spring.db.mysql.domains.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoimScheduleAndUserService {
	private final MoimScheduleAndUserRepository moimScheduleAndUserRepository;
	private final MoimScheduleAlarmRepository moimScheduleAlarmRepository;

	public void createAll(List<MoimScheduleAndUser> moimScheduleAndUsers, Moim moim) {
		validateMoimScheduleAndUserSize(moimScheduleAndUsers);
		validateMoimScheduleAndUsersInMoimAndUsers(moimScheduleAndUsers, moim);
		moimScheduleAndUserRepository.saveAll(moimScheduleAndUsers);
	}

	private void validateMoimScheduleAndUserSize(List<MoimScheduleAndUser> moimScheduleAndUsers) {
		if (moimScheduleAndUsers.size() == 0) {
			throw new GroupException(ErrorStatus.EMPTY_USERS_FAILURE);
		}
	}

	private void validateMoimScheduleAndUsersInMoimAndUsers(List<MoimScheduleAndUser> moimScheduleAndUsers, Moim moim) {
		Set<User> moimUsers = moim.getMoimAndUsers()
			.stream()
			.map(MoimAndUser::getUser)
			.collect(Collectors.toSet());
		for (MoimScheduleAndUser moimScheduleAndUser : moimScheduleAndUsers) {
			if (!moimUsers.contains(moimScheduleAndUser.getUser())) {
				throw new GroupException(ErrorStatus.NOT_USERS_IN_MOIM);
			}

		}
	}

	public void removeMoimScheduleAndUser(MoimSchedule moimSchedule) {
		moimScheduleAndUserRepository.deleteMoimScheduleAndUserByMoimSchedule(moimSchedule);
	}

	public void removeMoimScheduleAndUserInPersonalSpace(MoimScheduleAndUser moimScheduleAndUser) {
		moimScheduleAndUser.handleDeletedPersonalSchedule();
	}

	public void removeMoimScheduleAndUsers(List<MoimScheduleAndUser> moimScheduleAndUsers) {
		moimScheduleAndUserRepository.deleteAll(moimScheduleAndUsers);
	}

	public MoimScheduleAndUser getMoimScheduleAndUser(MoimSchedule moimSchedule, User user) {
		return moimScheduleAndUserRepository.findMoimScheduleAndUserByMoimScheduleAndUser(moimSchedule, user)
			.orElseThrow(() -> new GroupException(ErrorStatus.NOT_FOUND_MOIM_SCHEDULE_AND_USER_FAILURE));
	}

	public List<MoimScheduleAndUser> getAllByUser(User user) {
		return moimScheduleAndUserRepository.findAllByUser(user);
	}

	public void removeMoimScheduleAlarm(MoimScheduleAndUser moimScheduleAndUser) {
		moimScheduleAlarmRepository.deleteMoimScheduleAlarmByMoimScheduleAndUser(moimScheduleAndUser);
	}

	public void removeMoimScheduleAlarm(List<MoimScheduleAndUser> moimScheduleAndUser) {
		moimScheduleAlarmRepository.deleteMoimScheduleAlarmByMoimScheduleAndUser(moimScheduleAndUser);
	}

	public void removeMoimScheduleAlarms(List<MoimScheduleAndUser> moimScheduleAndUsers) {
		moimScheduleAndUsers.forEach(moimScheduleAndUser ->
			moimScheduleAlarmRepository.deleteAll(moimScheduleAndUser.getMoimScheduleAlarms()));
	}

	public void createMoimScheduleAlarm(MoimScheduleAlarm moimScheduleAlarm) {
		moimScheduleAlarmRepository.save(moimScheduleAlarm);
	}

	/**
	 * 메서드 네이밍에 더 분명한 의미를 담기 위해서 규칙을 좀 더
	 * 자세히 세워보는 것도 괜찮을 것 같아요
	 */
	public List<MoimScheduleAndUser> getMoimScheduleAndUsersForMonthMoimMemo(User user, List<LocalDateTime> dates,
		Pageable page) {
		return moimScheduleAndUserRepository.findMoimScheduleMemoByMonthPaging(user, dates, page);
	}

	public void modifyText(MoimScheduleAndUser moimScheduleAndUser, String moimScheduleText) {
		moimScheduleAndUser.updateText(moimScheduleText);
	}

	public void removeMoimScheduleMemoInPersonalSpace(MoimScheduleAndUser moimScheduleAndUser) {
		moimScheduleAndUser.handleDeletedPersonalMoimMemo();
	}

	public MoimScheduleAndUser getMoimScheduleAndUser(Long moimScheduleId, User user) {
		MoimSchedule moimSchedule = MoimSchedule.builder().id(moimScheduleId).build();
		return getMoimScheduleAndUser(moimSchedule, user);
	}
}
