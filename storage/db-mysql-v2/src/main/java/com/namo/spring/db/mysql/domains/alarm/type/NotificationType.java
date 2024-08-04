package com.namo.spring.db.mysql.domains.alarm.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.namo.spring.db.mysql.common.converter.CodedEnum;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NotificationType implements CodedEnum {
	FRIEND_REQUEST("1", "친구 요청"),
	FRIEND_REQUEST_ACCEPTED("2", "친구 요청 수락"),
	FRIEND_REQUEST_REJECTED("3", "친구 요청 거절"),
	SCHEDULE_CREATED("4", "일정 생성"),
	SCHEDULE_UPDATED("5", "일정 수정"),
	SCHEDULE_DELETED("6", "일정 삭제"),
	SCHEDULE_TO_BE_STARTED("7", "일정 시작 예정");

	private final String code;
	private final String type;

	@JsonCreator
	public NotificationType fromString(String type) {
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
