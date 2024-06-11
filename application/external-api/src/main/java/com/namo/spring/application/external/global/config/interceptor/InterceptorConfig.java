package com.namo.spring.application.external.global.config.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

	private final AuthenticationInterceptor authenticationInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authenticationInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/api/v1/auths/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger/**", "/test/log", "/favicon.ico");
	}
}
