package com.namo.spring.db.mysql.domains.alarm.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.namo.spring.db.mysql.common.converter.CodedEnum;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PublisherType implements CodedEnum {
	MEMBER("1", "사용자"),
	SYSTEM("2", "시스템"),
	;

	private final String code;
	private final String type;

	@JsonCreator
	public PublisherType fromString(String type) {
		return valueOf(type.toUpperCase());
	}

	@Override
	public String getCode() {
		return code;
	}

	@JsonValue
	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
