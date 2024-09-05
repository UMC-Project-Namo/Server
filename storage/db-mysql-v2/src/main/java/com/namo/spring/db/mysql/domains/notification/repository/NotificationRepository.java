package com.namo.spring.db.mysql.domains.notification.repository;

import com.namo.spring.db.mysql.domains.notification.dto.ScheduleNotificationQuery;
import com.namo.spring.db.mysql.domains.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT new com.namo.spring.db.mysql.domains.notification.dto.ScheduleNotificationQuery( " +
            "n.id, n.schedule.id, n.notifyAt ) FROM Notification n " +
            "WHERE n.schedule.id IN :scheduleIds " +
            "AND n.notificationType = '7' " +
            "AND n.receiver.id = :memberId")
    List<ScheduleNotificationQuery> findNotificationsByReceiverIdAndScheduleIds(Long memberId, List<Long> scheduleIds);
}