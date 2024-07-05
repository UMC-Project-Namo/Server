package com.namo.spring.application.external.global.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.namo.spring.application.external.global.common.security.filter.JwtAuthenticationFilter;
import com.namo.spring.application.external.global.common.security.filter.JwtExceptionFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityAdapterConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
	private final DaoAuthenticationProvider daoAuthenticationProvider;

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtExceptionFilter jwtExceptionFilter;

	@Override
	public void configure(HttpSecurity builder) throws Exception {
		builder.authenticationProvider(daoAuthenticationProvider);
		builder.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		builder.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);
	}
}
