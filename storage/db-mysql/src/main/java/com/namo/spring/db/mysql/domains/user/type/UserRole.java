package com.namo.spring.db.mysql.domains.user.type;

import static java.util.stream.Collectors.*;

import java.util.Map;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserRole {
	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER");

	private static final Map<String, UserRole> stringToEnum =
		Stream.of(values()).collect(toMap(Object::toString, e -> e));

	private final String type;

	@JsonCreator
	public static UserRole fromString(String type) {
		return stringToEnum.get(type.toUpperCase());
	}

	@JsonValue
	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return type;
	}
}
