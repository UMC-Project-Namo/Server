package com.namo.spring.db.mysql.common.converter;

import com.namo.spring.db.mysql.domains.alarm.type.NotificationType;

public class NotificationTypeConverter extends AbstractEnumAttributeConverter<NotificationType> {
	private static final String ENUM_NAME = "알림 타입";

	public NotificationTypeConverter() {
		super(NotificationType.class, false, ENUM_NAME);
	}
}
