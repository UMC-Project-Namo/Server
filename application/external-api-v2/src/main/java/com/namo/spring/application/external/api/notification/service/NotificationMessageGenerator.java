package com.namo.spring.application.external.api.notification.service;

import java.util.HashMap;
import java.util.Map;

import com.namo.spring.application.external.global.utils.ReminderTimeUtils;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.namo.spring.db.mysql.domains.notification.type.NotificationType;

import lombok.RequiredArgsConstructor;

import static com.namo.spring.application.external.global.config.properties.ReminderTimeConfig.SCHEDULED_TIME_TRIGGER;

@Component
@RequiredArgsConstructor
public class NotificationMessageGenerator {

    private final Map<NotificationType, String> templates = new HashMap<>();

    @PostConstruct
    public void init() {
        templates.put(NotificationType.FRIEND_REQUEST, "%s님으로부터 친구 요청이 도착했어요");
        templates.put(NotificationType.FRIEND_REQUEST_ACCEPTED, "%s님이 친구 요청을 수락했어요");
        templates.put(NotificationType.SCHEDULE_REMINDER, "'%s' 일정이 %s");
        templates.put(NotificationType.GUEST_IS_INVITED, "'%s'에 새로운 게스트가 있습니다");
        templates.put(NotificationType.SCHEDULE_RECORD_REMINDER, "일정이 종료되었나요? 오늘 일정에 새로운 기록을 남겨보세요");
    }

    public String getScheduleReminderMessageBody(String title, String triggerTime) {
        String body = "";
        if(triggerTime.equals(SCHEDULED_TIME_TRIGGER)){
            body = "시작됩니다!";
        }
        else body = ReminderTimeUtils.toViewTime(triggerTime)+ " 남았어요!";
        return String.format(templates.get(NotificationType.SCHEDULE_REMINDER), title, body);
    }

    public String getFriendshipMessageBody(NotificationType type, String memberName) {
        return String.format(templates.get(type), memberName);
    }

}
