package com.namo.spring.application.external.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class NotificationResponse {
    private NotificationResponse() {
        throw new IllegalStateException("Utility class");
    }

    @Getter
    @Builder
    public static class GetNotificationSettingInfoDto {
        @Schema(description = "푸시 알림 활성화 여부")
        private boolean notificationEnabled;
        @Schema(description = "기기 정보")
        private DeviceInfo deviceInfo;
    }


    @Getter
    @Builder
    public static class DeviceInfo {
        @Schema(description = "기기 ID")
        private Long deviceId;
        @Schema(description = "기기 type, WEB / IOS / ANDROID")
        private String deviceType;
        @Schema(description = "기기 토큰")
        private String deviceToken;
        @Schema(description = "기기 상세 정보")
        private String deviceAgent;
    }

}
