package com.namo.spring.db.mysql.domains.notification.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.namo.spring.db.mysql.common.converter.NotificationTypeConverter;
import com.namo.spring.db.mysql.common.converter.PublisherTypeConverter;
import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.notification.type.NotificationType;
import com.namo.spring.db.mysql.domains.notification.type.PublisherType;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Column(length = 20)
    private String publisherName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Member publisher;

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
    public Notification(PublisherType publisherType, String publisherName, Member publisher, Member receiver,
            Device device, Schedule schedule, LocalDateTime notifyAt, NotificationType notificationType,
            String notificationJson) {
        this.publisherType = Objects.requireNonNull(publisherType, "publisherType은 null일 수 없습니다.");
        this.publisherName = publisherName;
        this.publisher = publisher;
        this.device = Objects.requireNonNull(device, "device는 null일 수 없습니다.");
        this.schedule = schedule;
        this.notificationType = Objects.requireNonNull(notificationType, "notificationType은 null일 수 없습니다.");
        this.notificationJson = Objects.requireNonNull(notificationJson, "notificationJson은 null일 수 없습니다.");
        this.notifyAt = notifyAt;
        this.isRead = false;
        this.isDeleted = false;
    }

    public static Notification of(PublisherType publisherType, Member publisher,
            Device device, Schedule schedule, LocalDateTime notifyAt, NotificationType notificationType,
            String notificationJson) {
        return Notification.builder()
                .publisherType(publisherType)
                .publisher(publisher)
                .publisherName(publisher != null ? publisher.getNickname() : null)
                .device(device)
                .schedule(schedule)
                .notifyAt(notifyAt)
                .notificationType(notificationType)
                .notificationJson(notificationJson)
                .build();
    }
}
