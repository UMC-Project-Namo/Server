package com.namo.spring.client.social.common;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.namo.spring.client.social")
public class FeignConfig {
}
