package com.namo.spring.application.external.domain.group.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.domain.group.application.converter.MoimAndUserConverter;
import com.namo.spring.application.external.domain.group.application.converter.MoimScheduleConverter;
import com.namo.spring.application.external.domain.group.application.converter.MoimScheduleResponseConverter;
import com.namo.spring.application.external.domain.group.application.impl.MoimAndUserService;
import com.namo.spring.application.external.domain.group.application.impl.MoimMemoLocationService;
import com.namo.spring.application.external.domain.group.application.impl.MoimMemoService;
import com.namo.spring.application.external.domain.group.application.impl.MoimScheduleAndUserService;
import com.namo.spring.application.external.domain.group.application.impl.MoimScheduleService;
import com.namo.spring.application.external.domain.group.application.impl.MoimService;
import com.namo.spring.application.external.domain.group.domain.Moim;
import com.namo.spring.application.external.domain.group.domain.MoimAndUser;
import com.namo.spring.application.external.domain.group.domain.MoimMemo;
import com.namo.spring.application.external.domain.group.domain.MoimMemoLocation;
import com.namo.spring.application.external.domain.group.domain.MoimMemoLocationImg;
import com.namo.spring.application.external.domain.group.domain.MoimSchedule;
import com.namo.spring.application.external.domain.group.domain.MoimScheduleAlarm;
import com.namo.spring.application.external.domain.group.domain.MoimScheduleAndUser;
import com.namo.spring.application.external.domain.group.ui.dto.GroupScheduleRequest;
import com.namo.spring.application.external.domain.group.ui.dto.GroupScheduleResponse;
import com.namo.spring.application.external.domain.individual.application.impl.CategoryService;
import com.namo.spring.application.external.domain.individual.application.impl.ScheduleService;
import com.namo.spring.application.external.domain.individual.domain.Category;
import com.namo.spring.application.external.domain.individual.domain.Schedule;
import com.namo.spring.application.external.domain.individual.domain.constant.Location;
import com.namo.spring.application.external.domain.individual.domain.constant.Period;
import com.namo.spring.application.external.domain.user.application.impl.UserService;
import com.namo.spring.db.mysql.domains.user.domain.User;
import com.namo.spring.application.external.global.common.constant.FilePath;
import com.namo.spring.application.external.global.utils.FileUtils;

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
