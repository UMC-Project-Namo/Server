package com.namo.spring.application.external.api.notification.service;


import com.namo.spring.db.mysql.domains.notification.dto.ScheduleNotificationQuery;
import com.namo.spring.db.mysql.domains.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationManageService {
    private final NotificationService notificationService;

    public List<ScheduleNotificationQuery> getScheduleNotifications(Long memberId, List<Long> scheduleIds) {
        return notificationService.readNotificationsByReceiverIdAndScheduleIds(memberId, scheduleIds);
    }
}
