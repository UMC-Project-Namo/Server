package com.namo.spring.db.mysql.domains.user.type;

import com.fasterxml.jackson.annotation.JsonValue;
import com.namo.spring.db.mysql.common.converter.CodedEnum;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberStatus implements CodedEnum {
	INACTIVE("비활성화", "1"),
	ACTIVE("활성화", "2"),
	PENDING("회원가입 대기", "3");

	private final String type;
	private final String code;

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
