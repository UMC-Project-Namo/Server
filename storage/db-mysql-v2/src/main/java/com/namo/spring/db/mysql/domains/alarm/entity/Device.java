package com.namo.spring.db.mysql.domains.alarm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.namo.spring.db.mysql.common.converter.ReceiverDeviceTypeConverter;
import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.alarm.type.ReceiverDeviceType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Device extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Convert(converter = ReceiverDeviceTypeConverter.class)
	@Column(name = "receiver_device_type", nullable = false, length = 50)
	private ReceiverDeviceType receiverDeviceType;

	@Column(name = "receiver_device_token")
	private String receiverDeviceToken;

	@Column(name = "receiver_device_agent")
	private String receiverDeviceAgent;

	@Builder
	public Device(ReceiverDeviceType receiverDeviceType, String receiverDeviceToken, String receiverDeviceAgent) {
		this.receiverDeviceType = receiverDeviceType;
		this.receiverDeviceToken = receiverDeviceToken;
		this.receiverDeviceAgent = receiverDeviceAgent;
	}
}
