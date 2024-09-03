package com.namo.spring.db.mysql.domains.notification.entity;

import com.namo.spring.db.mysql.common.converter.NotificationTypeConverter;
import com.namo.spring.db.mysql.common.converter.PublisherTypeConverter;
import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.notification.type.NotificationType;
import com.namo.spring.db.mysql.domains.notification.type.PublisherType;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Convert(converter = PublisherTypeConverter.class)
    @Column(nullable = false, length = 50)
    private PublisherType publisherType;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false, length = 20)
    private String publisherName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Member publisher;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Convert(converter = NotificationTypeConverter.class)
    @Column(nullable = false, length = 50)
    private NotificationType notificationType;

    @Column(nullable = false)
    private String notificationJson;

    @Column(nullable = false)
    private boolean isRead;

    // 일정 시작 알림일 때 설정
    private LocalDateTime notifyAt;

    private LocalDateTime readAt;

    private boolean isDeleted;

    @Builder
    public Notification(PublisherType publisherType, String publisherName, Member receiver,
                        Device device, Schedule schedule, LocalDateTime notifyAt, NotificationType notificationType, String notificationJson) {
        this.publisherType = publisherType;
        this.publisherName = publisherName;
        this.receiver = receiver;
        this.device = device;
        this.schedule = schedule;
        this.notificationType = notificationType;
        this.notificationJson = notificationJson;
        this.notifyAt = notifyAt;
        this.isRead = false;
        this.isDeleted = false;
    }
}
