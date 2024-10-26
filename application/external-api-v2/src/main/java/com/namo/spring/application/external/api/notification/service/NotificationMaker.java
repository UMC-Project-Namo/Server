package com.namo.spring.application.external.api.notification.service;

import static com.namo.spring.application.external.api.notification.converter.NotificationConverter.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.namo.spring.application.external.global.utils.ReminderTimeUtils;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.notification.entity.Device;
import com.namo.spring.db.mysql.domains.notification.entity.Notification;
import com.namo.spring.db.mysql.domains.notification.exception.NotificationException;
import com.namo.spring.db.mysql.domains.notification.service.NotificationService;
import com.namo.spring.db.mysql.domains.notification.type.NotificationType;
import com.namo.spring.db.mysql.domains.notification.type.ReceiverDeviceType;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationMaker {
    private final NotificationService notificationService;
    private final FcmJsonMessageBuilder fcmJsonMessageBuilder;
    private final NotificationMessageGenerator notificationMessageGenerator;

    /**
     * 일정 예정 알림 생성
     *
     * @param schedule
     * @param devices
     * @param remiderMap 일정 예정으로 부터 남은 시간 & 알림 시간 Map
     */
    public void makeScheduleReminderNotifications(Schedule schedule, List<Device> devices,
                                                  Map<String, LocalDateTime> remiderMap) {
        List<Notification> newNotifications = devices.stream()
                .flatMap(device -> remiderMap.entrySet().stream()
                        .map(entry -> {
                            try {
                                String triggerTime = entry.getKey();
                                String title = schedule.getTitle();
                                String body = notificationMessageGenerator.getScheduleReminderMessageBody(
                                        schedule.getTitle(),
                                        ReminderTimeUtils.toViewTime(triggerTime));
                                String message = makeFcmMessage(device, title, body);
                                LocalDateTime reminderTime = entry.getValue();
                                return toScheduleReminderNotification(message, device, schedule, reminderTime);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException("JSON 메세지 에러 발생", e);
                            }
                        }))
                .collect(Collectors.toList());
        notificationService.createNotifications(newNotifications);
    }

    public void makeScheduleUpdateNotifications(NotificationType type, Schedule schedule, List<Device> devices) {
        String title = "";
        String body = "";
        List<Notification> newNotifications = devices.stream()
                .map(device -> {
                    try {
                        String message = makeFcmMessage(device, title, body);
                        return toScheduleUpdateNotification(message, type, schedule, device);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("JSON 메세지 에러 발생", e);
                    }
                })
                .collect(Collectors.toList());
        notificationService.createNotifications(newNotifications);
    }

    public void makeFriendshipNotification(NotificationType type, Member publisher, List<Device> recieverDevices) {
        String messageTitle = type.getType();
        String messageBody = notificationMessageGenerator.getFriendshipMessageBody(type, publisher.getName());
        List<Notification> newNotifications = recieverDevices.stream()
                .map(device -> {
                    try {
                        return toFriendshipNotification(makeFcmMessage(device, messageTitle, messageBody), type, publisher, device);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("JSON 메세지 에러 발생", e);
                    }
                })
                .collect(Collectors.toList());
        notificationService.createNotifications(newNotifications);
    }

    private String makeFcmMessage(Device device, String title, String body) throws JsonProcessingException {
        if (device.getReceiverDeviceType().equals(ReceiverDeviceType.ANDROID)) {
            return fcmJsonMessageBuilder.buildAndroidFcmMessage(device.getReceiverDeviceToken(), title, body, null);
        } else if (device.getReceiverDeviceType().equals(ReceiverDeviceType.IOS)) {
            return fcmJsonMessageBuilder.buildIOSFcmMessage(device.getReceiverDeviceToken(), title, body, null, 0);
        } else
            throw new NotificationException(ErrorStatus.NOT_SUPPORTED_DEVICE_TYPE);
    }

}
