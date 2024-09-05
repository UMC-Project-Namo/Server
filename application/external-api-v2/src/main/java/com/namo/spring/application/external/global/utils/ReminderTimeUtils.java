package com.namo.spring.application.external.global.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ReminderTimeUtils {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");


    public static List<LocalDateTime> toLocalDateTimes(LocalDateTime baseTime, List<String> reminderTriggers) {
        return reminderTriggers.stream()
                .map(reminderTime -> baseTime.minusMinutes(toMinutes(reminderTime)))
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
