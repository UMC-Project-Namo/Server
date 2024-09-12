package com.namo.spring.application.external.global.common.security.jwt;

public record CustomJwts(
        String accessToken,
        String refreshToken
) {
    public static CustomJwts of(String accessToken, String refreshToken) {
        return new CustomJwts(accessToken, refreshToken);
    }
}
