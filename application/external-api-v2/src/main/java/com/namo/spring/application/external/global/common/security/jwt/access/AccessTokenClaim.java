package com.namo.spring.application.external.global.common.security.jwt.access;

import java.util.Map;

import com.namo.spring.core.infra.common.jwt.JwtClaims;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessTokenClaim implements JwtClaims {
    private final Map<String, Object> claims;

    public static AccessTokenClaim of(Long userId) {
        Map<String, Object> claims = Map.of(
                AccessTokenClaimKeys.USER_ID.getValue(), userId.toString()
        );
        return new AccessTokenClaim(claims);
    }

    @Override
    public Map<String, Object> getClaims() {
        return claims;
    }
}
