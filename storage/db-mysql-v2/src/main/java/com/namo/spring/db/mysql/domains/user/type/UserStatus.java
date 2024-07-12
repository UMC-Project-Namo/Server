package com.namo.spring.db.mysql.domains.user.type;

import com.fasterxml.jackson.annotation.JsonValue;
import com.namo.spring.db.mysql.common.converter.CodedEnum;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserStatus implements CodedEnum {
	INACTIVE("0", "비활성화"),
	ACTIVE("1", "활성화");

	private final String code;
	private final String type;

	@Override
	public String toString() {
		return this.type;
	}

	@Override
	public String getCode() {
		return this.code;
	}

	public String getType() {
		return this.type;
	}

	@JsonValue
	public String createJson() {
		return this.name();
	}
}
