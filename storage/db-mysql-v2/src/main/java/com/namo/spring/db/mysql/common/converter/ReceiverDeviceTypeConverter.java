package com.namo.spring.db.mysql.common.converter;

import com.namo.spring.db.mysql.domains.alarm.type.ReceiverDeviceType;

public class ReceiverDeviceTypeConverter extends AbstractEnumAttributeConverter<ReceiverDeviceType> {
	private static final String ENUM_NAME = "수신자 디바이스 타입";

	public ReceiverDeviceTypeConverter() {
		super(ReceiverDeviceType.class, false, ENUM_NAME);
	}
}
