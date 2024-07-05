package com.namo.spring.application.external.global.config.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.namo.spring.core.infra.common.properties.ServerDomainProperties;

@Configuration
@EnableConfigurationProperties({
	ServerDomainProperties.class
})
public class PropertiesConfig {
}
