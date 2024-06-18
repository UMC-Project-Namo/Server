package com.namo.spring.application.external.domain.individual.application;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.domain.group.application.impl.MoimScheduleAndUserService;
import com.namo.spring.application.external.domain.group.application.impl.MoimScheduleService;
import com.namo.spring.application.external.domain.group.domain.MoimSchedule;
import com.namo.spring.application.external.domain.group.domain.MoimScheduleAndUser;
import com.namo.spring.application.external.domain.individual.application.converter.AlarmConverter;
import com.namo.spring.application.external.domain.individual.application.converter.ScheduleConverter;
import com.namo.spring.application.external.domain.individual.application.converter.ScheduleResponseConverter;
import com.namo.spring.application.external.domain.individual.application.impl.AlarmService;
import com.namo.spring.application.external.domain.individual.application.impl.CategoryService;
import com.namo.spring.application.external.domain.individual.application.impl.ImageService;
import com.namo.spring.application.external.domain.individual.application.impl.PeriodService;
import com.namo.spring.application.external.domain.individual.application.impl.ScheduleService;
import com.namo.spring.application.external.domain.individual.domain.Alarm;
import com.namo.spring.application.external.domain.individual.domain.Category;
import com.namo.spring.application.external.domain.individual.domain.Image;
import com.namo.spring.application.external.domain.individual.domain.Schedule;
import com.namo.spring.application.external.domain.individual.domain.constant.Period;
import com.namo.spring.application.external.domain.individual.ui.dto.ScheduleRequest;
import com.namo.spring.application.external.domain.individual.ui.dto.ScheduleResponse;
import com.namo.spring.application.external.domain.user.application.impl.UserService;
import com.namo.spring.db.mysql.domains.user.domain.User;
import com.namo.spring.application.external.global.common.constant.FilePath;
import com.namo.spring.application.external.global.utils.FileUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScheduleFacade {
	private final Logger logger = LoggerFactory.getLogger(ScheduleFacade.class);
	private final UserService userService;
	private final ScheduleService scheduleService;
	private final AlarmService alarmService;
	private final ImageService imageService;
	private final CategoryService categoryService;
	private final MoimScheduleService moimScheduleService;
	private final PeriodService periodService;
	private final MoimScheduleAndUserService moimScheduleAndUserService;
	private final FileUtils fileUtils;

	@Transactional
	public ScheduleResponse.ScheduleIdDto createSchedule(ScheduleRequest.PostScheduleDto req, Long userId) {
		User user = userService.getUser(userId);
		Category category = categoryService.getCategory(req.getCategoryId());
		categoryService.validateUsersCategory(userId, category);

		Period period = ScheduleConverter.toPeriod(req);
		periodService.checkValidDate(period);
		Schedule schedule = ScheduleConverter.toSchedule(req, period, user, category);
		List<Alarm> alarms = AlarmConverter.toAlarms(req, schedule);
		alarmService.checkValidAlarm(alarms);
		schedule.addAlarms(alarms);
		Schedule saveSchedule = scheduleService.createSchedule(schedule);

		return ScheduleResponseConverter.toScheduleIdRes(saveSchedule);
	}

	@Transactional(readOnly = true)
	public List<ScheduleResponse.GetScheduleDto> getSchedulesByUser(Long userId,
		List<LocalDateTime> localDateTimes) {
		User user = userService.getUser(userId);
		return scheduleService.getSchedulesByUserId(user, localDateTimes.get(0), localDateTimes.get(1));
	}

	@Transactional(readOnly = true)
	public List<ScheduleResponse.GetScheduleDto> getMoimSchedulesByUser(Long userId,
		List<LocalDateTime> localDateTimes) {
		User user = userService.getUser(userId);
		return scheduleService.getMoimSchedulesByUser(user, localDateTimes.get(0), localDateTimes.get(1));
	}

	@Transactional(readOnly = true)
	public List<ScheduleResponse.GetScheduleDto> getAllSchedulesByUser(Long userId) {
		User user = userService.getUser(userId);
		return scheduleService.getAllSchedulesByUser(user);
	}

	@Transactional(readOnly = true)
	public List<ScheduleResponse.GetScheduleDto> getAllMoimSchedulesByUser(Long userId) {
		User user = userService.getUser(userId);
		return scheduleService.getAllMoimSchedulesByUser(user);
	}

	@Transactional
	public ScheduleResponse.ScheduleIdDto modifySchedule(
		Long scheduleId,
		ScheduleRequest.PostScheduleDto req,
		Long userId
	) {
		Schedule schedule = scheduleService.getScheduleById(scheduleId);
		Category category = categoryService.getCategory(req.getCategoryId());
		categoryService.validateUsersCategory(userId, category);
		Period period = ScheduleConverter.toPeriod(req);
		periodService.checkValidDate(period);

		schedule.clearAlarm();
		List<Alarm> alarms = AlarmConverter.toAlarms(req, schedule);
		alarmService.checkValidAlarm(alarms);
		schedule.addAlarms(alarms);

		schedule.updateSchedule(
			req.getName(),
			period,
			category,
			req.getX(),
			req.getY(),
			req.getLocationName(),
			req.getKakaoLocationId()
		);

		return ScheduleResponseConverter.toScheduleIdRes(schedule);
	}

	@Transactional
	public void removeSchedule(Long scheduleId, Integer kind, Long userId) {
		if (kind == 0) { // 개인 스케줄 :스케줄 알람, 이미지 함께 삭제
			Schedule schedule = scheduleService.getScheduleById(scheduleId);
			alarmService.removeAlarmsBySchedule(schedule);
			List<String> urls = schedule.getImages().stream()
				.map(Image::getImgUrl)
				.toList();
			fileUtils.deleteImages(urls, FilePath.INVITATION_ACTIVITY_IMG);
			imageService.removeImgsBySchedule(schedule);
			scheduleService.removeSchedule(schedule);
			return;
		}
		User user = userService.getUser(userId);
		MoimSchedule moimSchedule = moimScheduleService.getMoimScheduleWithMoimScheduleAndUsers(scheduleId);
		MoimScheduleAndUser moimScheduleAndUser = moimScheduleAndUserService.getMoimScheduleAndUser(moimSchedule, user);

		moimScheduleAndUserService.removeMoimScheduleAlarm(moimScheduleAndUser);
		moimScheduleAndUserService.removeMoimScheduleAndUserInPersonalSpace(moimScheduleAndUser);
	}

}
