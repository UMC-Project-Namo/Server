package com.namo.spring.core.infra.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app.image.url")
public class ImageUrlProperties {
    private final String meeting;
}
