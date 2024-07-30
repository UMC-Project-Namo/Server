package com.namo.spring.application.external.api.individual.facade;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.group.service.GroupScheduleAndUserService;
import com.namo.spring.application.external.api.group.service.GroupScheduleService;
import com.namo.spring.application.external.api.individual.converter.AlarmConverter;
import com.namo.spring.application.external.api.individual.converter.ScheduleConverter;
import com.namo.spring.application.external.api.individual.converter.ScheduleResponseConverter;
import com.namo.spring.application.external.api.individual.dto.ScheduleRequest;
import com.namo.spring.application.external.api.individual.dto.ScheduleResponse;
import com.namo.spring.application.external.api.individual.service.AlarmService;
import com.namo.spring.application.external.api.individual.service.CategoryService;
import com.namo.spring.application.external.api.individual.service.ImageService;
import com.namo.spring.application.external.api.individual.service.PeriodService;
import com.namo.spring.application.external.api.individual.service.ScheduleService;
import com.namo.spring.application.external.api.user.service.UserService;
import com.namo.spring.core.infra.common.aws.s3.FileUtils;
import com.namo.spring.core.infra.common.constant.FilePath;
import com.namo.spring.db.mysql.domains.group.domain.MoimSchedule;
import com.namo.spring.db.mysql.domains.group.domain.MoimScheduleAndUser;
import com.namo.spring.db.mysql.domains.individual.domain.Alarm;
import com.namo.spring.db.mysql.domains.individual.domain.Category;
import com.namo.spring.db.mysql.domains.individual.domain.Image;
import com.namo.spring.db.mysql.domains.individual.domain.Schedule;
import com.namo.spring.db.mysql.domains.individual.type.Period;
import com.namo.spring.db.mysql.domains.user.domain.User;

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
	private final GroupScheduleService groupScheduleService;
	private final PeriodService periodService;
	private final GroupScheduleAndUserService groupScheduleAndUserService;
	private final FileUtils fileUtils;

	@Transactional
	public ScheduleResponse.ScheduleIdDto createSchedule(ScheduleRequest.PostScheduleDto req, Long userId) {
		User user = userService.getUser(userId);
		Category category = getValidatedCategory(req.getCategoryId(), userId);
		Period period = getValidatedPeriod(req);
		Schedule schedule = createScheduleWithAlarms(req, period, user, category);
		return ScheduleResponseConverter.toScheduleIdRes(schedule);
	}

	@Transactional(readOnly = true)
	public List<ScheduleResponse.GetScheduleDto> getSchedulesByUser(Long userId, List<LocalDateTime> localDateTimes) {
		User user = userService.getUser(userId);
		return scheduleService.getAllSchedulesByUser(user, localDateTimes.get(0), localDateTimes.get(1));
	}

	@Transactional(readOnly = true)
	public List<ScheduleResponse.GetMeetingScheduleDto> getGroupSchedulesByUser(Long userId,
		List<LocalDateTime> localDateTimes) {
		User user = userService.getUser(userId);
		return scheduleService.getGroupSchedulesByUser(user, localDateTimes.get(0), localDateTimes.get(1));
	}

	@Transactional(readOnly = true)
	public List<ScheduleResponse.GetScheduleDto> getAllSchedulesByUser(Long userId) {
		User user = userService.getUser(userId);
		return scheduleService.getAllSchedulesByUser(user, null, null);
	}

	@Transactional(readOnly = true)
	public List<ScheduleResponse.GetScheduleDto> getAllGroupSchedulesByUser(Long userId) {
		User user = userService.getUser(userId);
		return scheduleService.getAllGroupSchedulesByUser(user);
	}

	@Transactional
	public ScheduleResponse.ScheduleIdDto modifySchedule(Long scheduleId, ScheduleRequest.PostScheduleDto req,
		Long userId) {
		Schedule schedule = scheduleService.getScheduleById(scheduleId);
		Category category = getValidatedCategory(req.getCategoryId(), userId);
		Period period = getValidatedPeriod(req);
		updateScheduleWithAlarms(schedule, req, period, category);
		return ScheduleResponseConverter.toScheduleIdRes(schedule);
	}

	@Transactional
	public void removeSchedule(Long scheduleId, Integer kind, Long userId) {
		if (kind == 0) {
			removePersonalSchedule(scheduleId);
		} else {
			removeGroupSchedule(scheduleId, userId);
		}
	}

	private Category getValidatedCategory(Long categoryId, Long userId) {
		Category category = categoryService.getCategory(categoryId);
		categoryService.validateUsersCategory(userId, category);
		return category;
	}

	private Period getValidatedPeriod(ScheduleRequest.PostScheduleDto req) {
		Period period = ScheduleConverter.toPeriod(req);
		periodService.checkValidDate(period);
		return period;
	}

	private Schedule createScheduleWithAlarms(ScheduleRequest.PostScheduleDto req, Period period, User user,
		Category category) {
		Schedule schedule = ScheduleConverter.toSchedule(req, period, user, category);
		List<Alarm> alarms = AlarmConverter.toAlarms(req, schedule);
		alarmService.checkValidAlarm(alarms);
		schedule.addAlarms(alarms);
		return scheduleService.createSchedule(schedule);
	}

	private void updateScheduleWithAlarms(Schedule schedule, ScheduleRequest.PostScheduleDto req, Period period,
		Category category) {
		schedule.clearAlarm();
		List<Alarm> alarms = AlarmConverter.toAlarms(req, schedule);
		alarmService.checkValidAlarm(alarms);
		schedule.addAlarms(alarms);
		schedule.updateSchedule(req.getName(), period, category, req.getX(), req.getY(), req.getLocationName(),
			req.getKakaoLocationId());
	}

	private void removePersonalSchedule(Long scheduleId) {
		Schedule schedule = scheduleService.getScheduleById(scheduleId);
		alarmService.removeAlarmsBySchedule(schedule);
		List<String> urls = schedule.getImages().stream()
			.map(Image::getImgUrl)
			.toList();
		fileUtils.deleteImages(urls, FilePath.INVITATION_ACTIVITY_IMG);
		scheduleService.removeSchedule(schedule);
	}

	private void removeGroupSchedule(Long scheduleId, Long userId) {
		User user = userService.getUser(userId);
		MoimSchedule groupSchedule = groupScheduleService.getGroupScheduleWithGroupScheduleAndUsers(scheduleId);
		MoimScheduleAndUser groupScheduleAndUser = groupScheduleAndUserService.getGroupScheduleAndUser(groupSchedule,
			user);
		groupScheduleAndUserService.removeGroupScheduleAlarm(groupScheduleAndUser);
		groupScheduleAndUserService.removeGroupScheduleAndUserInPersonalSpace(groupScheduleAndUser);
	}
}
