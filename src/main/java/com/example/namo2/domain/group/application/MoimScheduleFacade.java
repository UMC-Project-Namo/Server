package com.example.namo2.domain.group.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.namo2.domain.individual.application.impl.CategoryService;
import com.example.namo2.domain.individual.domain.Category;

import com.example.namo2.domain.group.application.impl.MoimMemoLocationService;
import com.example.namo2.domain.group.application.impl.MoimMemoService;
import com.example.namo2.domain.group.domain.MoimMemo;
import com.example.namo2.domain.group.domain.MoimMemoLocation;
import com.example.namo2.domain.group.domain.MoimMemoLocationImg;

import com.example.namo2.domain.group.application.converter.MoimAndUserConverter;
import com.example.namo2.domain.group.application.converter.MoimScheduleConverter;
import com.example.namo2.domain.group.application.converter.MoimScheduleResponseConverter;
import com.example.namo2.domain.group.application.impl.MoimAndUserService;
import com.example.namo2.domain.group.application.impl.MoimScheduleAndUserService;
import com.example.namo2.domain.group.application.impl.MoimScheduleService;
import com.example.namo2.domain.group.application.impl.MoimService;
import com.example.namo2.domain.group.domain.Moim;
import com.example.namo2.domain.group.domain.MoimAndUser;
import com.example.namo2.domain.group.domain.MoimSchedule;
import com.example.namo2.domain.group.domain.MoimScheduleAlarm;
import com.example.namo2.domain.group.domain.MoimScheduleAndUser;
import com.example.namo2.domain.group.ui.dto.GroupScheduleRequest;
import com.example.namo2.domain.group.ui.dto.GroupScheduleResponse;

import com.example.namo2.domain.individual.application.impl.ScheduleService;
import com.example.namo2.domain.individual.domain.constant.Location;
import com.example.namo2.domain.individual.domain.constant.Period;
import com.example.namo2.domain.individual.domain.Schedule;

import com.example.namo2.domain.user.application.impl.UserService;
import com.example.namo2.domain.user.domain.User;

import com.example.namo2.global.common.constant.FilePath;
import com.example.namo2.global.utils.FileUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MoimScheduleFacade {
	private final UserService userService;
	private final MoimService moimService;
	private final MoimAndUserService moimAndUserService;
	private final MoimScheduleService moimScheduleService;
	private final MoimScheduleAndUserService moimScheduleAndUserService;
	private final MoimMemoService moimMemoService;
	private final MoimMemoLocationService moimMemoLocationService;
	private final ScheduleService scheduleService;
	private final CategoryService categoryService;

	private final FileUtils fileUtils;

	/**
	 * 버그 발생 우려;
	 * categories 수정시 모임과 기본 카테고리에 대해서는 수정이 불가능하게 해야함
	 */
	@Transactional(readOnly = false)
	public Long createSchedule(GroupScheduleRequest.PostGroupScheduleDto moimScheduleDto) {
		Moim moim = moimService.getMoimWithMoimAndUsersByMoimId(moimScheduleDto.getGroupId());
		Period period = MoimScheduleConverter.toPeriod(moimScheduleDto);
		Location location = MoimScheduleConverter.toLocation(moimScheduleDto);
		MoimSchedule moimSchedule = MoimScheduleConverter
			.toMoimSchedule(moim, period, location, moimScheduleDto);
		MoimSchedule savedMoimSchedule = moimScheduleService.create(moimSchedule);

		createMoimScheduleAndUsers(moimScheduleDto.getUsers(), savedMoimSchedule, moim);

		return savedMoimSchedule.getId();
	}

	private void createMoimScheduleAndUsers(List<Long> usersId, MoimSchedule savedMoimSchedule, Moim moim) {
		List<User> users = userService.getUsersInMoimSchedule(usersId);
		List<Category> categories = categoryService
			.getMoimUsersCategories(users);
		List<MoimScheduleAndUser> moimScheduleAndUsers = MoimScheduleConverter
			.toMoimScheduleAndUsers(categories, savedMoimSchedule, users);
		moimScheduleAndUserService.createAll(moimScheduleAndUsers, moim);
	}

	@Transactional(readOnly = false)
	public void modifyMoimSchedule(GroupScheduleRequest.PatchGroupScheduleDto moimScheduleDto) {
		MoimSchedule moimSchedule = moimScheduleService.getMoimSchedule(moimScheduleDto.getMoimScheduleId());
		Period period = MoimScheduleConverter.toPeriod(moimScheduleDto);
		Location location = MoimScheduleConverter.toLocation(moimScheduleDto);
		moimSchedule.update(moimScheduleDto.getName(), period, location);
		moimScheduleAndUserService.removeMoimScheduleAndUser(moimSchedule);
		createMoimScheduleAndUsers(moimScheduleDto.getUsers(), moimSchedule, moimSchedule.getMoim());
	}

	@Transactional(readOnly = false)
	public void modifyMoimScheduleCategory(GroupScheduleRequest.PatchGroupScheduleCategoryDto scheduleCategoryDto,
		Long userId) {
		MoimSchedule moimSchedule = moimScheduleService.getMoimSchedule(scheduleCategoryDto.getMoimScheduleId());
		User user = userService.getUser(userId);
		Category category = categoryService.getCategory(scheduleCategoryDto.getCategoryId());
		MoimScheduleAndUser moimScheduleAndUser = moimScheduleAndUserService.getMoimScheduleAndUser(moimSchedule, user);
		moimScheduleAndUser.updateCategory(category);
	}

	@Transactional(readOnly = false)
	public void removeMoimSchedule(Long moimScheduleId, Long userId) {
		MoimSchedule moimSchedule = moimScheduleService.getMoimScheduleWithMoimMemo(moimScheduleId);
		List<MoimScheduleAndUser> moimScheduleAndUsers = moimScheduleService.getMoimScheduleAndUsers(moimSchedule);

		existMoimAndUser(userId, moimSchedule.getMoim());

		removeMoimScheduleMemo(moimSchedule.getMoimMemo());

		moimScheduleAndUserService.removeMoimScheduleAlarm(moimScheduleAndUsers);
		moimScheduleAndUserService.removeMoimScheduleAndUser(moimSchedule);
		moimScheduleService.remove(moimSchedule);
	}

	private void existMoimAndUser(Long userId, Moim moim) {
		User user = userService.getUser(userId);
		moimAndUserService.getMoimAndUser(moim, user);
	}

	private void removeMoimScheduleMemo(MoimMemo moimMemo) {
		if (moimMemo == null) {
			return;
		}
		List<MoimMemoLocation> moimMemoLocations = moimMemoLocationService.getMoimMemoLocationWithImgs(moimMemo);
		moimMemoLocationService.removeMoimMemoLocationAndUsers(moimMemoLocations);
		removeMoimMemoLocationImgs(moimMemoLocations);
		moimMemoService.removeMoimMemo(moimMemo);
	}

	private void removeMoimMemoLocationImgs(List<MoimMemoLocation> moimMemoLocations) {
		List<MoimMemoLocationImg> moimMemoLocationImgs
			= moimMemoLocationService.getMoimMemoLocationImgs(moimMemoLocations);
		List<String> urls = moimMemoLocationImgs.stream()
			.map(MoimMemoLocationImg::getUrl)
			.toList();
		fileUtils.deleteImages(urls, FilePath.GROUP_ACTIVITY_IMG);
		moimMemoLocationService.removeMoimMemoLocationImgs(moimMemoLocations);
	}

	@Transactional(readOnly = false)
	public void createMoimScheduleAlarm(GroupScheduleRequest.PostGroupScheduleAlarmDto moimScheduleAlarmDto,
		Long userId) {
		MoimSchedule moimSchedule = moimScheduleService.getMoimSchedule(moimScheduleAlarmDto.getMoimScheduleId());
		User user = userService.getUser(userId);
		MoimScheduleAndUser moimScheduleAndUser = moimScheduleAndUserService.getMoimScheduleAndUser(moimSchedule, user);

		for (Integer alarmDate : moimScheduleAlarmDto.getAlarmDates()) {
			MoimScheduleAlarm moimScheduleAlarm = MoimScheduleConverter.toMoimScheduleAlarm(moimScheduleAndUser,
				alarmDate);
			moimScheduleAndUserService.createMoimScheduleAlarm(moimScheduleAlarm);
		}
	}

	@Transactional(readOnly = false)
	public void modifyMoimScheduleAlarm(GroupScheduleRequest.PostGroupScheduleAlarmDto moimScheduleAlarmDto,
		Long userId) {
		MoimSchedule moimSchedule = moimScheduleService.getMoimSchedule(moimScheduleAlarmDto.getMoimScheduleId());
		User user = userService.getUser(userId);
		MoimScheduleAndUser moimScheduleAndUser = moimScheduleAndUserService.getMoimScheduleAndUser(moimSchedule, user);
		moimScheduleAndUserService.removeMoimScheduleAlarm(moimScheduleAndUser);

		for (Integer alarmDate : moimScheduleAlarmDto.getAlarmDates()) {
			MoimScheduleAlarm moimScheduleAlarm = MoimScheduleConverter.toMoimScheduleAlarm(moimScheduleAndUser,
				alarmDate);
			moimScheduleAndUserService.createMoimScheduleAlarm(moimScheduleAlarm);
		}
	}

	@Transactional(readOnly = true)
	public List<GroupScheduleResponse.MoimScheduleDto> getMonthMoimSchedules(Long moimId,
		List<LocalDateTime> localDateTimes, Long userId) {
		Moim moim = moimService.getMoimWithMoimAndUsersByMoimId(moimId);
		existMoimAndUser(userId, moim);
		List<MoimAndUser> moimAndUsersInMoim = moimAndUserService.getMoimAndUsers(moim);
		List<User> users = MoimAndUserConverter.toUsers(moimAndUsersInMoim);

		List<Schedule> indivisualsSchedules = scheduleService.getSchedules(users);
		List<MoimScheduleAndUser> moimScheduleAndUsers = moimScheduleService
			.getMonthMoimSchedules(localDateTimes, users);
		return MoimScheduleResponseConverter
			.toMoimScheduleDtos(indivisualsSchedules, moimScheduleAndUsers, moimAndUsersInMoim);
	}

	@Transactional(readOnly = true)
	public List<GroupScheduleResponse.MoimScheduleDto> getAllMoimSchedules(Long moimId, Long userId) {
		Moim moim = moimService.getMoimWithMoimAndUsersByMoimId(moimId);
		existMoimAndUser(userId, moim);
		List<MoimAndUser> moimAndUsersInMoim = moimAndUserService.getMoimAndUsers(moim);
		List<User> users = MoimAndUserConverter.toUsers(moimAndUsersInMoim);

		List<Schedule> indivisualsSchedules = scheduleService.getSchedules(users);
		List<MoimScheduleAndUser> moimScheduleAndUsers = moimScheduleService
			.getAllMoimSchedules(users);
		return MoimScheduleResponseConverter
			.toMoimScheduleDtos(indivisualsSchedules, moimScheduleAndUsers, moimAndUsersInMoim);
	}
}
