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
public class GroupScheduleAndUserService {
	private final MoimScheduleAndUserRepository moimScheduleAndUserRepository;
	private final MoimScheduleAlarmRepository moimScheduleAlarmRepository;

	public void createAll(List<MoimScheduleAndUser> moimScheduleAndUsers, Moim moim) {
		validateGroupScheduleAndUserSize(moimScheduleAndUsers);
		validateGroupScheduleAndUsersInGroupAndUsers(moimScheduleAndUsers, moim);
		moimScheduleAndUserRepository.saveAll(moimScheduleAndUsers);
	}

	private void validateGroupScheduleAndUserSize(List<MoimScheduleAndUser> moimScheduleAndUsers) {
		if (moimScheduleAndUsers.size() == 0) {
			throw new GroupException(ErrorStatus.EMPTY_USERS_FAILURE);
		}
	}

	private void validateGroupScheduleAndUsersInGroupAndUsers(List<MoimScheduleAndUser> moimScheduleAndUsers,
		Moim moim) {
		Set<User> moimUsers = moim.getMoimAndUsers()
			.stream()
			.map(MoimAndUser::getUser)
			.collect(Collectors.toSet());
		for (MoimScheduleAndUser moimScheduleAndUser : moimScheduleAndUsers) {
			if (!moimUsers.contains(moimScheduleAndUser.getUser())) {
				throw new GroupException(ErrorStatus.NOT_USERS_IN_GROUP);
			}

		}
	}

	public void removeGroupScheduleAndUser(MoimSchedule moimSchedule) {
		moimScheduleAndUserRepository.deleteMoimScheduleAndUserByMoimSchedule(moimSchedule);
	}

	public void removeGroupScheduleAndUserInPersonalSpace(MoimScheduleAndUser moimScheduleAndUser) {
		moimScheduleAndUser.handleDeletedPersonalSchedule();
	}

	public void removeGroupScheduleAndUsers(List<MoimScheduleAndUser> moimScheduleAndUsers) {
		moimScheduleAndUserRepository.deleteAll(moimScheduleAndUsers);
	}

	public MoimScheduleAndUser getGroupScheduleAndUser(MoimSchedule moimSchedule, User user) {
		return moimScheduleAndUserRepository.findMoimScheduleAndUserByMoimScheduleAndUser(moimSchedule, user)
			.orElseThrow(() -> new GroupException(ErrorStatus.NOT_FOUND_GROUP_SCHEDULE_AND_USER_FAILURE));
	}

	public List<MoimScheduleAndUser> getAllByUser(User user) {
		return moimScheduleAndUserRepository.findAllByUser(user);
	}

	public void removeGroupScheduleAlarm(MoimScheduleAndUser moimScheduleAndUser) {
		moimScheduleAlarmRepository.deleteMoimScheduleAlarmByMoimScheduleAndUser(moimScheduleAndUser);
	}

	public void removeGroupScheduleAlarm(List<MoimScheduleAndUser> moimScheduleAndUser) {
		moimScheduleAlarmRepository.deleteMoimScheduleAlarmByMoimScheduleAndUser(moimScheduleAndUser);
	}

	public void removeGroupScheduleAlarms(List<MoimScheduleAndUser> moimScheduleAndUsers) {
		moimScheduleAndUsers.forEach(moimScheduleAndUser ->
			moimScheduleAlarmRepository.deleteAll(moimScheduleAndUser.getMoimScheduleAlarms()));
	}

	public void createGroupScheduleAlarm(MoimScheduleAlarm moimScheduleAlarm) {
		moimScheduleAlarmRepository.save(moimScheduleAlarm);
	}

	/**
	 * 메서드 네이밍에 더 분명한 의미를 담기 위해서 규칙을 좀 더
	 * 자세히 세워보는 것도 괜찮을 것 같아요
	 */
	public List<MoimScheduleAndUser> getGroupScheduleAndUsersForMonthGroupMemo(User user, List<LocalDateTime> dates,
		Pageable page) {
		return moimScheduleAndUserRepository.findMoimScheduleMemoByMonthPaging(user, dates, page);
	}

	public void modifyText(MoimScheduleAndUser moimScheduleAndUser, String moimScheduleText) {
		moimScheduleAndUser.updateText(moimScheduleText);
	}

	public void removeGroupScheduleDiaryInPersonalSpace(MoimScheduleAndUser moimScheduleAndUser) {
		moimScheduleAndUser.handleDeletedPersonalMoimMemo();
	}

	public MoimScheduleAndUser getGroupScheduleAndUser(Long moimScheduleId, User user) {
		MoimSchedule moimSchedule = MoimSchedule.builder().id(moimScheduleId).build();
		return getGroupScheduleAndUser(moimSchedule, user);
	}
}
