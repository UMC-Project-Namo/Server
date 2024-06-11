package com.namo.spring.client.social.naver.config;

import org.springframework.context.annotation.Bean;

import com.namo.spring.client.social.naver.decoder.NaverFeignClientExceptionDecoder;

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
