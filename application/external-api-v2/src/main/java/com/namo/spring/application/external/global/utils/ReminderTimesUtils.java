package com.namo.spring.application.external.global.utils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReminderTimesUtils {
    public static List<LocalDateTime> toLocalDateTimes(LocalDateTime baseTime, List<String> reminderTriggers) {
        return reminderTriggers.stream()
                .map(reminderTime -> baseTime.minusMinutes(toMinutes(reminderTime)))
                .collect(Collectors.toList());
    }

    public static int toMinutes(String trigger) {
        if (trigger == null || trigger.length() < 2) {
            throw new IllegalArgumentException("유효하지 않은 값 입니다.");
        }

        char unit = trigger.charAt(0);
        int value = Integer.parseInt(trigger.substring(1));

        return switch (unit) {
            case 'M' -> value;
            case 'H' -> value * 60;
            case 'D' -> value * 24 * 60;
            default -> throw new IllegalArgumentException("에러 발생");
        };
    }
}
