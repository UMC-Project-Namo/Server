package com.namo.spring.application.external.api.notification.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.namo.spring.application.external.api.schedule.service.ParticipantManageService;
import com.namo.spring.application.external.global.utils.ReminderTimeUtils;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.notification.dto.ScheduleNotificationQuery;
import com.namo.spring.db.mysql.domains.notification.entity.Device;
import com.namo.spring.db.mysql.domains.notification.exception.NotificationException;
import com.namo.spring.db.mysql.domains.notification.service.DeviceService;
import com.namo.spring.db.mysql.domains.notification.service.NotificationService;
import com.namo.spring.db.mysql.domains.notification.type.NotificationType;
import com.namo.spring.db.mysql.domains.notification.type.ReceiverDeviceType;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationManageService {
    private static final NotificationType SCHDULE_REMINDER_NOTOFICATION_TYPE = NotificationType.SCHEDULE_REMINDER;
    private static final List<NotificationType> SCHEDULE_UPDATE_NOTIFICATION_TYPES = Arrays.asList(
            NotificationType.SCHEDULE_CREATED, NotificationType.SCHEDULE_UPDATED, NotificationType.SCHEDULE_DELETED);
    private static final List<NotificationType> FRIENDSHIP_NOTIFICATION_TYPES = Arrays.asList(
            NotificationType.FRIEND_REQUEST, NotificationType.FRIEND_REQUEST_ACCEPTED,
            NotificationType.FRIEND_REQUEST_REJECTED);
    private final NotificationMaker notificationMaker;
    private final NotificationService notificationService;
    private final DeviceService deviceService;
    private final ParticipantManageService participantManageService;

    public List<ScheduleNotificationQuery> getScheduleNotifications(Long memberId, List<Participant> schedules) {
        List<Long> scheduleIds = schedules.stream()
                .map(Participant::getSchedule)
                .map(Schedule::getId)
                .collect(Collectors.toList());
        return notificationService.readNotificationsByReceiverIdAndScheduleIds(memberId, scheduleIds);
    }

    public void updateOrCreateScheduleReminderNotification(Long scheduleId, Member member, List<String> triggers) {
        Participant participant = participantManageService.getValidatedParticipantWithSchedule(member.getId(),
                scheduleId);
        notificationService.deleteScheduleNotificationsByScheduleAndReceiver(participant.getSchedule().getId(),
                member.getId(), NotificationType.SCHEDULE_REMINDER);
        if (!triggers.isEmpty()) {
            createScheduleReminderNotification(participant.getSchedule(), member, triggers);
        }
    }

    public void createScheduleReminderNotification(Schedule schedule, Member member, List<String> triggers) {
        List<LocalDateTime> reminderTimes = ReminderTimeUtils.toLocalDateTimes(schedule.getPeriod().getStartDate(),
                triggers);
        List<Device> devices = getMobileDevices(member.getId());
        notificationMaker.makeScheduleReminderNotifications(schedule, devices, reminderTimes);
    }

    public void createFriendshipNotification(NotificationType type, Member publisher, Member reciever) throws
            JsonProcessingException {
        if (!FRIENDSHIP_NOTIFICATION_TYPES.contains(type)) {
            throw new IllegalArgumentException("에러 발생");
        }
        List<Device> devices = getMobileDevices(reciever.getId());
        notificationMaker.makeFriendshipNotification(type, publisher, devices);
    }

    private List<Device> getMobileDevices(Long recieverId) {
        List<Device> mobileDevices = getDeviceByMember(recieverId).stream()
                .filter(device -> !device.getReceiverDeviceType().equals(ReceiverDeviceType.WEB))
                .collect(Collectors.toList());
        if (mobileDevices.isEmpty()) {
            throw new NotificationException(ErrorStatus.NOT_FOUND_MOBILE_DEVICE_FAILURE);
        }
        return mobileDevices;
    }

    private List<Device> getDeviceByMember(Long recieverId) {
        List<Device> devices = deviceService.readByMemberId(recieverId);
        if (devices.isEmpty()) {
            throw new NotificationException(ErrorStatus.NOT_FOUND_DEVICE_FAILURE);
        }
        return devices;
    }

}
