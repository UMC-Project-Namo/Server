package com.namo.spring.db.mysql.domains.notification.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ScheduleNotificationQuery {
    private Long notificationId;
    private Long scheduleId;
    private LocalDateTime notifyAt;
}
