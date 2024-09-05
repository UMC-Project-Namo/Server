package com.namo.spring.application.external.api.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FcmJsonMessageBuilder {
    //TODO : 기획 바탕으로 json 구조 수정 필요 - 주원
    private final ObjectMapper objectMapper;

    public String buildAndroidFcmMessage(String token, String title, String body, Map<String, String> data) throws JsonProcessingException {
        Map<String, Object> message = new HashMap<>();
        message.put("token", token);

        Map<String, String> notification = new HashMap<>();
        notification.put("title", title);
        notification.put("body", body);
        message.put("notification", notification);

        if (data != null && !data.isEmpty()) {
            message.put("data", data);
        }

        Map<String, Object> fcmMessage = new HashMap<>();
        fcmMessage.put("message", message);

        return objectMapper.writeValueAsString(fcmMessage);
    }

    public String buildIOSFcmMessage(String token, String title, String body, Map<String, Object> data, int badge) throws JsonProcessingException {
        Map<String, Object> message = new HashMap<>();
        message.put("token", token);

        Map<String, Object> notification = new HashMap<>();
        notification.put("title", title);
        notification.put("body", body);
        message.put("notification", notification);

        Map<String, Object> apns = new HashMap<>();
        Map<String, Object> apnsPayload = new HashMap<>();
        Map<String, Object> aps = new HashMap<>();

        aps.put("alert", notification);
        aps.put("badge", badge);
        aps.put("sound", "default");

        apnsPayload.put("aps", aps);
        if (data != null && !data.isEmpty()) {
            apnsPayload.putAll(data);
        }

        apns.put("payload", apnsPayload);
        message.put("apns", apns);

        Map<String, Object> fcmMessage = new HashMap<>();
        fcmMessage.put("message", message);

        return objectMapper.writeValueAsString(fcmMessage);
    }
}
