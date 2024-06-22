package com.namo.spring.application.external.global.common.security.jwt.access;

import lombok.Getter;

@Getter
public enum AccessTokenClaimKeys {
	USER_ID("id");

	private final String value;

	AccessTokenClaimKeys(String value) {
		this.value = value;
	}
}
