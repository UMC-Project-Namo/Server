package com.namo.spring.application.external.api.notification.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;

import com.namo.spring.db.mysql.domains.notification.type.NotificationType;

class NotificationMessageGeneratorTest {

    @InjectMocks
    private NotificationMessageGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new NotificationMessageGenerator();
        generator.init(); // PostConstruct 메서드 수동 호출
    }

    @Test
    void testGetScheduleReminderTemplate() {
        String scheduleTitle = "팀 미팅";
        String startTime = "09:00";
        String expected = "09:00 팀 미팅";

        String result = generator.getScheduleReminderTemplate(startTime, scheduleTitle);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @EnumSource(value = NotificationType.class, names = {"FRIEND_REQUEST", "FRIEND_REQUEST_ACCEPTED"})
    void testGetFriendshipTemplate(NotificationType type) {
        String memberName = "나몽";
        String result = generator.getFriendshipTemplate(type, memberName);

        switch (type) {
            case FRIEND_REQUEST:
                assertEquals("나몽님이 친구 요청을 보냈습니다.", result);
                break;
            case FRIEND_REQUEST_ACCEPTED:
                assertEquals("나몽님이 친구 요청을 수락하였습니다.", result);
                break;
            default:
                fail("Unexpected NotificationType: " + type);
        }
    }

    @Test
    void init() {
        assertNotNull(generator.getScheduleReminderTemplate("시간", "제목"));
        assertNotNull(generator.getFriendshipTemplate(NotificationType.FRIEND_REQUEST, "테스트"));
        assertNotNull(generator.getFriendshipTemplate(NotificationType.FRIEND_REQUEST_ACCEPTED, "테스트"));
    }
}
