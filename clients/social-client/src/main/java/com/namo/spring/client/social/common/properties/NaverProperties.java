package com.namo.spring.client.social.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "spring.security.oauth1.client.registration.naver")
@Getter
@Setter
public class NaverProperties {
	private String clientId;
	private String clientSecret;
}
