package com.namo.spring.db.mysql.domains.alarm.entity;

import java.time.LocalDateTime;

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
import com.namo.spring.db.mysql.domains.alarm.type.NotificationType;
import com.namo.spring.db.mysql.domains.alarm.type.PublisherType;
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

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "publisher_id")
	private Member publisher;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(nullable = false, length = 20)
	private String publisherName;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "receiver_id", nullable = false)
	private Member receiver;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "device_id", nullable = false)
	private Device device;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Convert(converter = NotificationTypeConverter.class)
	@Column(nullable = false, length = 50)
	private NotificationType notificationType;

	@Column(nullable = false)
	private String notificationJson;

	@Column(nullable = false)
	private boolean isRead;

	private LocalDateTime readAt;

	private boolean isDeleted;

	@Builder
	public Notification(PublisherType publisherType, Member publisher, String publisherName, Member receiver,
		Device device, NotificationType notificationType, String notificationJson) {
		this.publisherType = publisherType;
		this.publisher = publisher;
		this.publisherName = publisherName;
		this.receiver = receiver;
		this.notificationType = notificationType;
		this.notificationJson = notificationJson;
		this.isRead = false;
		this.isDeleted = false;
	}
}
