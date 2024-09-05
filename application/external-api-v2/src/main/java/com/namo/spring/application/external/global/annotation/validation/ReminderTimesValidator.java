package com.namo.spring.application.external.global.annotation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

import static com.namo.spring.application.external.global.config.properties.ReminderTimeConfig.NOTIFICATION_TIME_PATTERN;
import static com.namo.spring.application.external.global.config.properties.ReminderTimeConfig.SCHEDULED_TIME_FIXED_VALUE;

public class ReminderTimesValidator implements ConstraintValidator<ValidReminderTimes, List<String>> {

    /**
     * 일정 예정 알림 시간의 request 형식을 검증합니다.
     * 정시 -> 'ST'
     * 분 -> 'M{1-59 까지의 숫자}'
     * 시간 -> 'H{1-36 까지의 숫자}'
     * 일 -> 'D{1-7까지의 숫자}'
     */

    @Override
    public boolean isValid(List<String> reminderTimes, ConstraintValidatorContext context) {
        if (reminderTimes == null || reminderTimes.isEmpty()) {
            return true;
        }
        for (String reminderTime : reminderTimes) {
            boolean isValid;
            if (!reminderTime.equals(SCHEDULED_TIME_FIXED_VALUE) && !NOTIFICATION_TIME_PATTERN.matcher(reminderTime).matches()) {
                return false;
            }
        }
        return true;
    }
}
