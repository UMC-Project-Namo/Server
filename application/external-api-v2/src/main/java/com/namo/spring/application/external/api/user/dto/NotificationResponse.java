package com.namo.spring.application.external.api.user.dto;

import lombok.Builder;
import lombok.Getter;

public class NotificationResponse {
    private NotificationResponse() {
        throw new IllegalStateException("Utility class");
    }

    @Getter
    @Builder
    public static class GetNotificationSettingInfoDto {
        private boolean notificationEnabled;
        private Long deviceId;
        private String deviceType;
        private String deviceToken;
    }


}
