package com.namo.spring.application.external.global.config.security;

public class WebSecurityUrl {
	private WebSecurityUrl() {
		throw new IllegalStateException("Utility class");
	}

	protected static final String[] READ_ONLY_PUBLIC_ENDPOINTS = {"/favicon.ico"};
	protected static final String[] PUBLIC_ENDPOINTS = {"/test/**"};
	protected static final String[] AUTHENTICATED_ENDPOINTS = {};
	protected static final String[] ANONYMOUS_ENDPOINTS = {"/api/v1/auths/**"};
	protected static final String[] SWAGGER_ENDPOINTS = {"/api-docs/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger"};
}
