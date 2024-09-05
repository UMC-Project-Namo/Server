package com.namo.spring.application.external.api.notification.service;

import com.namo.spring.db.mysql.domains.notification.type.NotificationType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationMessageGenerator {
    //TODO : 기획 바탕으로 메세지 템플릿 수정 필요 - 주원
    private final Map<NotificationType, String> templates = new HashMap<>();

    @PostConstruct
    public void init() {
        templates.put(NotificationType.FRIEND_REQUEST, "%s님이 친구 요청을 보냈습니다.");
        templates.put(NotificationType.FRIEND_REQUEST_ACCEPTED, "%s님이 친구 요청을 수락하였습니다.");
        templates.put(NotificationType.SCHEDULE_REMINDER, "'%s' 일정이 %s에 시작됩니다.");
    }

    public String getScheduleReminderTemplate(String scheduleTitle, String time) {
        return String.format(templates.get(NotificationType.SCHEDULE_REMINDER), scheduleTitle, time);
    }

    public String getFriendshipTemplate(NotificationType type, String memberName) {
        return String.format(templates.get(type), memberName);
    }

}
