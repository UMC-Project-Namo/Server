package com.namo.spring.application.external.global.feignclient.kakao;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KakaoFeignConfiguration {
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(KakaoFeignConfiguration.class);

	@Bean
	public RequestInterceptor requestInterceptor() {
		return template -> template.header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
	}

	@Bean
	public ErrorDecoder errorDecoder() {
		return new KakaoFeignClientExceptionDecoder();
	}

	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
}
