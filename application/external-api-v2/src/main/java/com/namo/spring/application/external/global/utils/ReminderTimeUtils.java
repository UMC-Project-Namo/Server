package com.namo.spring.application.external.global.utils;

import static com.namo.spring.application.external.global.config.properties.ReminderTimeConfig.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReminderTimeUtils {

    /**
     * 일정 시작 시간과 알림 트리거 목록을 기반으로 알림 시간 맵을 반환합니다.
     *
     * @param baseTime 일정 시작 시감
     * @param reminderTriggers 알림 트리거 문자열 목록.
     * @return 각 트리거 문자열을 키로, 계산된 알림 시간을 값으로 하는 Map.
     *         키가 SCHEDULED_TIME_TRIGGER인 경우 값은 baseTime과 동일함.
     *         그 외의 경우, 값은 baseTime에서 해당 트리거 시간만큼 뺀 시간.
     *
     */
    public static Map<String, LocalDateTime> toLocalDateTimeMap(LocalDateTime baseTime, List<String> reminderTriggers) {
        Map<String, LocalDateTime> resultMap = new HashMap<>(reminderTriggers.size());
        for (String reminderTrigger : reminderTriggers) {
            resultMap.put(toViewTime(reminderTrigger),
                    reminderTrigger.equals(SCHEDULED_TIME_TRIGGER)
                            ? baseTime
                            : baseTime.minusMinutes(toMinutes(reminderTrigger)));
        }
        return resultMap;
    }

    public static String toViewTime(String trigger) {
        char unit = trigger.charAt(0);
        int value = Integer.parseInt(trigger.substring(1));

        return switch (unit) {
            case 'M' -> value+"분";
            case 'H' -> value+"시간";
            case 'D' -> value+"일";
            default -> throw new IllegalArgumentException("유효하지 않은 시간 단위입니다. 'M', 'H', 'D' 중 하나여야 합니다");
        };
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

}
