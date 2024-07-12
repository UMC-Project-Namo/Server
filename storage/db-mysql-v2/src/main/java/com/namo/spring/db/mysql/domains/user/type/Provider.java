package com.namo.spring.db.mysql.domains.user.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.namo.spring.db.mysql.common.converter.CodedEnum;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Provider implements CodedEnum {
	KAKAO("1", "카카오"),
	NAVER("2", "네이버"),
	APPLE("3", "애플"),
	;

	private final String code;
	private final String type;

	@JsonCreator
	public Provider fromString(String type) {
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
