package com.namo.spring.application.external.global.config.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * 일정 예정 알림 시간 설정에 관련된 상수들을 정의하는 설정 클래스입니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReminderTimeConfig {

    /**
     * 알림 메세지를 위해 시간을 "HH:mm" 형식으로 포맷팅하기 위한 DateTimeFormatter입니다.
     * 예: "14:30"
     */
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * 정시 알림을 나타내는 트리거 값으로, 고정 값입니다.
     * 이 값은 다른 시간 트리거(예: "M30", "H1" 등)과 구분하기 위해 사용됩니다.
     */
    public static final String SCHEDULED_TIME_TRIGGER = "ST";

    /**
     * 유효한 알림 시간 패턴을 정의하는 정규 표현식입니다.
     * 허용되는 형식:
     * - M1 ~ M59: 1분에서 59분 전
     * - H1 ~ H36: 1시간에서 36시간 전
     * - D1 ~ D7: 1일에서 7일 전
     */
    public static final Pattern REMINDER_TRIGGER_PATTERN = Pattern.compile("^(M[1-9]|M[1-5][0-9]|H([1-9]|[1-2][0-9]|3[0-6])|D[1-7])$");
}
