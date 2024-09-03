package com.namo.spring.db.mysql.domains.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ScheduleNotificationQuery {
    private Long notificationId;
    private Long scheduleId;
    private LocalDateTime notifyAt;

}
