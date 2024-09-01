package com.namo.spring.db.mysql.domains.notification.service;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.notification.dto.ScheduleNotificationQuery;
import com.namo.spring.db.mysql.domains.notification.entity.Notification;
import com.namo.spring.db.mysql.domains.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@DomainService
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public Optional<Notification> readNotification(Long id) {
        return notificationRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ScheduleNotificationQuery> readNotificationsByReceiverIdAndScheduleIds(Long memberId, List<Long> scheduleIds) {
        return notificationRepository.findNotificationsByReceiverIdAndScheduleIds(memberId, scheduleIds);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}
