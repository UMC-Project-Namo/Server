package com.namo.spring.application.external.api.notification.service;

import static com.namo.spring.application.external.api.notification.dto.FcmNotificationRequest.*;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.namo.spring.application.external.api.notification.dto.FcmNotificationRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FcmJsonMessageBuilder {
    //TODO : 기획 바탕으로 json 구조 수정 필요 - 주원
    private final ObjectMapper objectMapper;

    public String buildAndroidFcmMessage(String token, String title, String body, Map<String, String> data) throws
            JsonProcessingException {
        FcmNotificationRequest.AndroidFcmMessageDto fcmMessage
                = toAndroidFcmMessageDto(token, title, body, data);

        return objectMapper.writeValueAsString(fcmMessage);
    }

    public String buildIOSFcmMessage(String token, String title, String body, Map<String, String> data,
            int badge) throws JsonProcessingException {
        FcmNotificationRequest.IosFcmMessageDto fcmMessage
                = toIosFcmMessageDto(token, title, body, data);

        return objectMapper.writeValueAsString(fcmMessage);
    }
}
