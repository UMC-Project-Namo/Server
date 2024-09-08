package com.namo.spring.application.external.api.notification.service;

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
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.namo.spring.application.external.api.notification.converter.NotificationConverter.*;

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
                                String title = notificationMessageGenerator.getScheduleReminderTemplate(ReminderTimeUtils.convertToString(schedule.getPeriod().getStartDate()), schedule.getTitle());
                                String body = "";
                                String message = makeFcmMessage(device, title, body);
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

    public void makeFriendshipNotification(NotificationType type, Member publisher, List<Device> recieverDevices) throws JsonProcessingException {
        String message = notificationMessageGenerator.getFriendshipTemplate(type, publisher.getName());
        List<Notification> newNotifications = recieverDevices.stream()
                .map(device -> toFriendshipNotification(message, type, publisher, device))
                .collect(Collectors.toList());
        notificationService.createNotifications(newNotifications);
    }

    private String makeFcmMessage(Device device, String title, String body) throws JsonProcessingException {
        if (device.getReceiverDeviceType().equals(ReceiverDeviceType.ANDROID)) {
            return fcmJsonMessageBuilder.buildAndroidFcmMessage(device.getReceiverDeviceToken(), title, body, null);
        } else if (device.getReceiverDeviceType().equals(ReceiverDeviceType.IOS)) {
            return fcmJsonMessageBuilder.buildIOSFcmMessage(device.getReceiverDeviceToken(), title, body, null, 0);
        } else throw new NotificationException(ErrorStatus.NOT_SUPPORTED_DEVICE_TYPE);
    }

}
