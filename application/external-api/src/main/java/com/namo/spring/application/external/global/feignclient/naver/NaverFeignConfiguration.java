package com.namo.spring.application.external.global.feignclient.naver;

import org.springframework.context.annotation.Bean;

import feign.Logger;
import feign.codec.ErrorDecoder;

public class NaverFeignConfiguration {
	@Bean
	ErrorDecoder errorDecoder() {
		return new NaverFeignClientExceptionDecoder();
	}

	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
}
