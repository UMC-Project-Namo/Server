package com.namo.spring.application.external.api.notification.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

public class FcmNotificationRequest {
    private FcmNotificationRequest() {
        throw new IllegalStateException("Util Class");
    }

    @Getter
    @Builder
    public static class Notification {
        private String title;
        private String body;
    }

    public static Notification toNotification(String title, String body) {
        return Notification.builder()
                .title(title)
                .body(body)
                .build();
    }

    @Getter
    @Builder
    public static class AndroidFcmMessageDto {
        private String token;
        private Notification notification;
        private Map<String, String> data;
        private AndroidConfig android;

        @Getter
        @Builder
        public static class AndroidConfig {
            private String priority;
            private AndroidNotification notification;
        }

        @Getter
        @Builder
        public static class AndroidNotification {
            private String clickAction;
            private String icon;
            private String color;
        }
    }

    public static AndroidFcmMessageDto toAndroidFcmMessageDto(String token, String title, String body, Map<String, String> data) {
        return AndroidFcmMessageDto.builder()
                .token(token)
                .notification(toNotification(title, body))
                .android(null)
                .data(data)
                .build();
    }


    @Getter
    @Builder
    public static class IosFcmMessageDto {
        private String token;
        private Notification notification;
        private Map<String, String> data;
        private ApnsConfig apns;

        @Getter
        @Builder
        public static class ApnsConfig {
            private ApnsPayload payload;
        }

        @Getter
        @Builder
        public static class ApnsPayload {
            private Aps aps;
        }

        @Getter
        @Builder
        public static class Aps {
            private String alert;
            private String sound;
            private Integer badge;
            private String category;
        }
    }

    public static IosFcmMessageDto toIosFcmMessageDto(String token, String title, String body, Map<String, String> data) {
        return IosFcmMessageDto.builder()
                .token(token)
                .notification(toNotification(title, body))
                .apns(null)
                .data(null)
                .build();
    }
}
