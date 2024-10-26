package com.namo.spring.application.external.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NotificationRequest {
    private NotificationRequest() {
        throw new IllegalStateException("Utility class");
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateDeviceInfoDto {
        @Schema(description = "기기 type, WEB / IOS / ANDROID")
        private String deviceType;
        @Schema(description = "기기 토큰")
        private String deviceToken;
        @Schema(description = "기기 상세 정보")
        private String deviceAgent;
    }
}
