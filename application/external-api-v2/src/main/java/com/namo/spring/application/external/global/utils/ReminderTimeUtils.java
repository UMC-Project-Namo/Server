package com.namo.spring.application.external.global.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.namo.spring.application.external.global.config.properties.ReminderTimeConfig.SCHEDULED_TIME_TRIGGER;
import static com.namo.spring.application.external.global.config.properties.ReminderTimeConfig.TIME_FORMATTER;

public class ReminderTimeUtils {

    public static List<LocalDateTime> toLocalDateTimes(LocalDateTime baseTime, List<String> reminderTriggers) {
        return reminderTriggers.stream()
                .map(reminderTime -> {
                    if (reminderTime.equals(SCHEDULED_TIME_TRIGGER)) return baseTime;
                    else return baseTime.minusMinutes(toMinutes(reminderTime));
                })
                .collect(Collectors.toList());
    }

    /**
     * 일정 예정 트리거를 분 단위의 정수로 변환합니다.
     * 트리거 문자열은 'M', 'H', 'D' 중 하나의 문자로 시작하며, 그 뒤에 숫자가 옵니다.
     * 'M'은 분, 'H'는 시간, 'D'는 일을 나타냅니다.
     * <p>
     * 예시:
     * - "M30" : 30분
     * - "H2"  : 2시간 (120분)
     * - "D1"  : 1일 (1440분)
     *
     * @param trigger 변환할 시간 트리거 문자열
     * @return 분 단위로 변환된 정수값
     */
    public static int toMinutes(String trigger) {
        if (trigger == null || trigger.length() < 2) {
            throw new IllegalArgumentException("길이가 너무 짧아 형식에 맞지 않습니다.");
        }

        char unit = trigger.charAt(0);
        int value = Integer.parseInt(trigger.substring(1));

        return switch (unit) {
            case 'M' -> value;
            case 'H' -> value * 60;
            case 'D' -> value * 24 * 60;
            default -> throw new IllegalArgumentException("유효하지 않은 시간 단위입니다. 'M', 'H', 'D' 중 하나여야 합니다");
        };
    }

    /**
     * LocalDateTime을 'D5', 'M1', 'ST' 등의 문자열로 변환합니다.
     *
     * @param reminderTime 변환할 LocalDateTime
     * @param baseTime     기준이 되는 LocalDateTime (일정 시작 시간)
     * @return 변환된 문자열 ('D5', 'M1', 'ST' 등)
     */
    public static String toReminderTrigger(LocalDateTime reminderTime, LocalDateTime baseTime) {
        if (reminderTime.isEqual(baseTime)) {
            return SCHEDULED_TIME_TRIGGER;
        }
        if (reminderTime.isAfter(baseTime)) {
            throw new IllegalArgumentException("알림 시간이 일정 시작 시간보다 이후입니다.");
        }
        long minutes = ChronoUnit.MINUTES.between(reminderTime, baseTime);

        if (minutes < 60) {
            return "M" + minutes;
        } else if (minutes < 24 * 60) {
            long hours = minutes / 60;
            return "H" + hours;
        } else {
            long days = minutes / (24 * 60);
            return "D" + days;
        }
    }

    /**
     * LocalDateTime에서 시간과 분을 추출하여 "HH:mm" 형식의 문자열로 반환합니다.
     *
     * @param dateTime 파싱할 LocalDateTime 객체
     * @return "HH:mm" 형식의 시간 문자열
     */
    public static String convertToString(LocalDateTime dateTime) {
        return dateTime.format(TIME_FORMATTER);
    }

}
