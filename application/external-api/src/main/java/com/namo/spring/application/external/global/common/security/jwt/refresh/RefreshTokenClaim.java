package com.namo.spring.application.external.global.common.security.jwt.refresh;

import java.util.Map;

import com.namo.spring.core.infra.common.jwt.JwtClaims;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshTokenClaim implements JwtClaims {
	private final Map<String, Object> claims;

	public static RefreshTokenClaim of(Long userId) {
		Map<String, Object> claims = Map.of(
			RefreshTokenClaimKeys.USER_ID.getValue(), userId.toString()
		);
		return new RefreshTokenClaim(claims);
	}

	@Override
	public Map<String, Object> getClaims() {
		return claims;
	}
}
