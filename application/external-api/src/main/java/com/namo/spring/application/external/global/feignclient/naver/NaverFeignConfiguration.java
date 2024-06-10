package com.example.namo2.global.feignclient.naver;

import org.springframework.context.annotation.Bean;

import feign.Logger;
import feign.codec.ErrorDecoder;

public class NaverFeignConfiguration {
	@Bean
	ErrorDecoder errorDecoder() {
		return new NaverFeignException();
	}

	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
}
