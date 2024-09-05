package com.namo.spring.application.external.api.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.namo.spring.application.external.global.utils.ReminderTimesUtils;
import com.namo.spring.db.mysql.domains.notification.entity.Device;
import com.namo.spring.db.mysql.domains.notification.entity.Notification;
import com.namo.spring.db.mysql.domains.notification.service.NotificationService;
import com.namo.spring.db.mysql.domains.notification.type.NotificationType;
import com.namo.spring.db.mysql.domains.notification.type.PublisherType;
import com.namo.spring.db.mysql.domains.notification.type.ReceiverDeviceType;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationMaker {
    private final NotificationService notificationService;
    private final FcmJsonMessageBuilder fcmJsonMessageBuilder;
    private final NotificationMessageGenerator notificationMessageGenerator;

    public void makeScheduleReminderNotifications(Schedule schedule, List<Device> devices, List<LocalDateTime> reminderTimes) {
        List<Notification> newNotifications = devices.stream()
                .flatMap(device -> reminderTimes.stream()
                        .map(reminderTime -> {
                            try {
                                return toScheduleReminderNotification(device, schedule, reminderTime);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException("에러 발생", e);
                            }
                        }))
                .collect(Collectors.toList());
        notificationService.createNotifications(newNotifications);
    }

    private Notification toScheduleReminderNotification(Device device, Schedule schedule, LocalDateTime notifyTime) throws JsonProcessingException {
        String title = notificationMessageGenerator.getScheduleReminderTemplate(schedule.getTitle(), ReminderTimesUtils.convertToString(notifyTime));
        String body = "";
        String message = makeFcmMessage(device, title, body);
        return Notification.of(PublisherType.SYSTEM, null, device.getMember(), device, schedule, notifyTime, NotificationType.SCHEDULE_REMINDER, message);
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
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        notificationService.createNotifications(newNotifications);
    }

    private Notification toScheduleUpdateNotification(String message, NotificationType type, Schedule schedule, Device device) throws JsonProcessingException {
        return Notification.of(PublisherType.SYSTEM, null, device.getMember(), device, schedule, null, type, message);
    }

    public void makeFriendshipNotification(NotificationType type, Member publisher, List<Device> recieverDevices) throws JsonProcessingException {
        String message = notificationMessageGenerator.getFriendshipTemplate(type, publisher.getName());
        List<Notification> newNotifications = recieverDevices.stream()
                .map(device -> Notification.of(PublisherType.MEMBER, publisher, device.getMember(), device, null, null, type, message))
                .collect(Collectors.toList());
        notificationService.createNotifications(newNotifications);
    }

    private String makeFcmMessage(Device device, String title, String body) throws JsonProcessingException {
        if (device.getReceiverDeviceType().equals(ReceiverDeviceType.ANDROID)) {
            return fcmJsonMessageBuilder.buildAndroidFcmMessage(device.getReceiverDeviceToken(), title, body, null);
        } else if (device.getReceiverDeviceType().equals(ReceiverDeviceType.IOS)) {
            return fcmJsonMessageBuilder.buildIOSFcmMessage(device.getReceiverDeviceToken(), title, body, null, 0);
        } else throw new IllegalArgumentException("에러 발생");
    }

}
