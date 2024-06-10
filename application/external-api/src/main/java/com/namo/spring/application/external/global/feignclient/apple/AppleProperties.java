package com.example.namo2.global.feignclient.apple;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.security.oauth1.client.registration.apple")
public class AppleProperties {
	private String clientId;
	private String teamId;
	private String keyId;
	private String privateKeyPath;
	private String redirectUri;
}
