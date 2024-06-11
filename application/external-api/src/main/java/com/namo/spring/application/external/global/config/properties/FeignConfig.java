package com.namo.spring.application.external.global.config.properties;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.example.namo2.global.feignclient")
public class FeignConfig {
}
