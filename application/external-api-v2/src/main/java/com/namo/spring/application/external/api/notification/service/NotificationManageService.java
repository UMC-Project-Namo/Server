package com.namo.spring.application.external.api.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.namo.spring.application.external.api.notification.converter.DeviceConverter;
import com.namo.spring.application.external.api.schedule.service.ParticipantManageService;
import com.namo.spring.application.external.api.user.dto.NotificationRequest;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationManageService {
    private static final NotificationType SCHEDULE_REMINDER_NOTIFICATION_TYPE = NotificationType.SCHEDULE_REMINDER;
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

    /**
     * 일정 리마인더 알림 생성
     */
    public void createScheduleReminderNotification(Schedule schedule, Member member, List<String> triggers) {
        Map<String, LocalDateTime> reminderMap = ReminderTimeUtils.toLocalDateTimeMap(schedule.getPeriod().getStartDate(),
                triggers);
        List<Device> devices = getMobileDevices(member.getId());
        notificationMaker.makeScheduleReminderNotifications(schedule, devices, reminderMap);
    }

    /**
     * 일정 리마인더 알림 수정
     */
    public void updateOrCreateScheduleReminderNotification(Long scheduleId, Member member, List<String> triggers) {
        Participant participant = participantManageService.getParticipantWithScheduleAndMember(member.getId(),
                scheduleId);
        notificationService.deleteScheduleNotificationsByScheduleAndReceiver(participant.getSchedule().getId(),
                member.getId(), NotificationType.SCHEDULE_REMINDER);
        if (!triggers.isEmpty()) {
            createScheduleReminderNotification(participant.getSchedule(), member, triggers);
        }
    }

    /**
     * 친구 신청 알림 생성
     */
    public void createFriendshipNotification(NotificationType type, Member publisher, Member reciever) throws
            JsonProcessingException {
        if (!FRIENDSHIP_NOTIFICATION_TYPES.contains(type)) {
            throw new IllegalArgumentException("에러 발생");
        }
        List<Device> devices = getMobileDevices(reciever.getId());
        notificationMaker.makeFriendshipNotification(type, publisher, devices);
    }

    /**
     * 기기 정보 등록 및 푸시 알림 활성화
     */
    public void createDeviceInfoAndNotificationEnabled(NotificationRequest.CreateDeviceInfoDto request, ReceiverDeviceType deviceType, Member member) {
        Device device = DeviceConverter.toDevice(request, deviceType, member);
        deviceService.createDevice(device);
    }

    /**
     * 유저의 푸시 알림 설정 정보 조회
     */
    public Device getNotificationSettingInfo(String deviceToken, Long memberId) {
        return deviceService.readDeviceByTokenAndMemberId(deviceToken, memberId)
                .orElseThrow(() -> new NotificationException(ErrorStatus.NOT_FOUND_MOBILE_DEVICE_FAILURE));
    }

    public void updateNotificationSetting(boolean request, Member member) {
        member.updateNotificationEnabled(request);
    }

    /**
     * 모바일 기기 정보 조회
     */
    private List<Device> getMobileDevices(Long recieverId) {
        List<Device> mobileDevices = getDeviceByMember(recieverId).stream()
                .filter(device -> !device.getReceiverDeviceType().equals(ReceiverDeviceType.WEB))
                .collect(Collectors.toList());
        if (mobileDevices.isEmpty()) {
            throw new NotificationException(ErrorStatus.NOT_FOUND_MOBILE_DEVICE_FAILURE);
        }
        return mobileDevices;
    }

    private List<Device> getDeviceByMember(Long memberId) {
        List<Device> devices = deviceService.readByMemberId(memberId);
        if (devices.isEmpty()) {
            throw new NotificationException(ErrorStatus.NOT_FOUND_DEVICE_FAILURE);
        }
        return devices;
    }

}
