package com.example.namo2.global.feignclient.apple;

import org.springframework.context.annotation.Bean;

import feign.Logger;
import feign.codec.ErrorDecoder;

public class AppleFeignConfiguration {
	@Bean
	ErrorDecoder errorDecoder() {
		return new AppleFeignException();
	}

	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
}
