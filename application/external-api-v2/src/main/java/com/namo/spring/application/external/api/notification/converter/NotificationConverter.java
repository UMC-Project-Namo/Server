package com.namo.spring.application.external.api.notification.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.namo.spring.db.mysql.domains.notification.entity.Device;
import com.namo.spring.db.mysql.domains.notification.entity.Notification;
import com.namo.spring.db.mysql.domains.notification.type.NotificationType;
import com.namo.spring.db.mysql.domains.notification.type.PublisherType;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import java.time.LocalDateTime;

public class NotificationConverter {
    public static Notification toScheduleReminderNotification(String message, Device device, Schedule schedule, LocalDateTime notifyTime) throws JsonProcessingException {
        return Notification.of(PublisherType.SYSTEM, null, device, schedule, notifyTime, NotificationType.SCHEDULE_REMINDER, message);
    }

    public static Notification toScheduleUpdateNotification(String message, NotificationType type, Schedule schedule, Device device) throws JsonProcessingException {
        return Notification.of(PublisherType.SYSTEM, null, device, schedule, null, type, message);
    }

    public static Notification toFriendshipNotification(String message, NotificationType type, Member publisher, Device device) {
        return Notification.of(PublisherType.MEMBER, publisher, device, null, null, type, message);
    }
}
