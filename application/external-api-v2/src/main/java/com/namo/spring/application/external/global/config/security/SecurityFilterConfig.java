package com.namo.spring.application.external.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.namo.spring.application.external.global.common.security.filter.JwtAuthenticationFilter;
import com.namo.spring.application.external.global.common.security.filter.JwtExceptionFilter;
import com.namo.spring.core.infra.common.jwt.JwtProvider;
import com.namo.spring.db.redis.cache.forbidden.ForbiddenTokenService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SecurityFilterConfig {
    private final UserDetailsService securityUserDetails;
    private final ForbiddenTokenService forbiddenTokenService;

    private final JwtProvider accessTokenProvider;

    private final ObjectMapper objectMapper;

    @Bean
    public JwtExceptionFilter jwtExceptionFilter() {
        return new JwtExceptionFilter(objectMapper);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(securityUserDetails, forbiddenTokenService, accessTokenProvider);
    }
}
