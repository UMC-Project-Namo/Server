package com.namo.spring.client.social.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth1.client.registration.apple")
public class AppleProperties {
	private String clientId;
	private String teamId;
	private String redirectUri;
	private String keyId;
	private String privateKeyPath;
	private String privateKey;
}
