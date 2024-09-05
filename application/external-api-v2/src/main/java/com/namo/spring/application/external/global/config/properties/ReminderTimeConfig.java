package com.namo.spring.application.external.global.config.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReminderTimeConfig {
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public static final String SCHEDULED_TIME_FIXED_VALUE = "ST";
    public static final Pattern NOTIFICATION_TIME_PATTERN = Pattern.compile("^(M[1-9]|M[1-5][0-9]|H([1-9]|[1-2][0-9]|3[0-6])|D[1-7])$");
}
