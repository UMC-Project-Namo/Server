package com.namo.spring.db.mysql.domains.user.type;

import static java.util.stream.Collectors.*;

import java.util.Map;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.namo.spring.db.mysql.common.converter.CodedEnum;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserRole implements CodedEnum {
	ADMIN("0","ROLE_ADMIN"),
	USER("1","ROLE_USER")
	;

	private static final Map<String, UserRole> stringToEnum =
		Stream.of(values()).collect(toMap(Object::toString, e -> e));

	private final String code;
	private final String type;

	@JsonCreator
	public static UserRole fromString(String type) {
		return stringToEnum.get(type.toUpperCase());
	}

	@JsonValue
	public String getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return this.type;
	}

	@Override
	public String getCode() {
		return this.code;
	}
}
