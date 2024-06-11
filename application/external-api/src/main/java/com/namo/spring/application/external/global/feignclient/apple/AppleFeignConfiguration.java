package com.namo.spring.application.external.global.feignclient.apple;

import org.springframework.context.annotation.Bean;

import feign.Logger;
import feign.codec.ErrorDecoder;

public class AppleFeignConfiguration {
	@Bean
	ErrorDecoder errorDecoder() {
		return new AppleFeignClientExceptionDecoder();
	}

	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
}
