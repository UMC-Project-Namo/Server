package com.namo.spring.core.infra.common.jwt;

import lombok.Getter;

@Getter
public enum AuthConstants {
	AUTHORIZATION("Authorization"), TOKEN_PREFIX("Bearer ");

	private final String value;

	AuthConstants(String value) {
		this.value = value;
	}
}
