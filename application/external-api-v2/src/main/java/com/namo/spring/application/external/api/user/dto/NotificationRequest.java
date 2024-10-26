package com.namo.spring.application.external.api.user.dto;

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
        private String receiverDeviceType;
        private String receiverDeviceToken;
        private String receiverDeviceAgent;
    }
}
