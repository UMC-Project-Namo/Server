package com.namo.spring.application.external.global.config.security;

public class WebSecurityUrl {
    private WebSecurityUrl() {
        throw new IllegalStateException("Utility class");
    }

    protected static final String[] READ_ONLY_PUBLIC_ENDPOINTS = {"/favicon.ico"};
    protected static final String[] PUBLIC_ENDPOINTS = {"/test/**", "/error"};
    protected static final String[] AUTHENTICATED_ENDPOINTS = {
            "/api/v1/auths/logout",
            "/api/v1/auths/kakao/delete", "/api/v1/auths/naver/delete", "/api/v1/auths/apple/delete"
    };
    protected static final String[] ANONYMOUS_ENDPOINTS = {"/api/v1/auths/**"};
    protected static final String[] SWAGGER_ENDPOINTS = {
            "/api-docs/**", "/v3/api-docs/**", "/swagger-ui/**",
            "/api/v1/swagger-ui/**", "/api/v1/swagger"
    };
    protected static final String[] REISSUANCE_ENDPOINTS = {"/api/v1/auths/reissuance"};
}
