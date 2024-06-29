package com.namo.spring.core.infra.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "server.domain")
public class ServerDomainProperties {
	private final String local;
	private final String service;
}
