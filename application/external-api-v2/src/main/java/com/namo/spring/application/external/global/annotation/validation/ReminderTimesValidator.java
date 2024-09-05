package com.namo.spring.application.external.global.annotation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.regex.Pattern;

public class ReminderTimesValidator implements ConstraintValidator<ValidReminderTimes, List<String>> {

    /**
     * 일정 예정 알림 시간의 request 형식을 검증합니다.
     * 정시 -> 'ST'
     * 분 -> 'M{1-59 까지의 숫자}'
     * 시간 -> 'H{1-36 까지의 숫자}'
     * 일 -> 'D{1-7까지의 숫자}'
     */

    private static final Pattern NOTIFICATION_TIME_PATTERN = Pattern.compile("^(M[1-9]|M[1-5][0-9]|H([1-9]|[1-2][0-9]|3[0-6])|D[1-7])$");
    private static final String SCHEDULED_TIME_FIXED_VALUE = "ST";

    @Override
    public boolean isValid(List<String> reminderTimes, ConstraintValidatorContext context) {
        if (reminderTimes == null || reminderTimes.isEmpty()) {
            return true;
        }
        for (String reminderTime : reminderTimes) {
            if (!reminderTimes.equals(SCHEDULED_TIME_FIXED_VALUE)) {
                return false;
            } else if (!NOTIFICATION_TIME_PATTERN.matcher(reminderTime).matches()) {
                return false;
            }
        }
        return true;
    }
}
