package com.namo.spring.client.social.apple.config;

import org.springframework.context.annotation.Bean;

import com.namo.spring.client.social.apple.decoder.AppleFeignClientExceptionDecoder;

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
