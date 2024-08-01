package com.namo.spring.core.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

    private DateUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * LocalDate를 Date로 변환
     *
     * @param localDate: LocalDate
     * @return Date
     */
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime를 Date로 변환
     *
     * @param localDateTime: LocalDateTime
     * @return Date
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date를 LocalDate로 변환
     *
     * @param date: Date
     * @return LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date를 LocalDateTime로 변환
     *
     * @param date: Date
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * LocalDateTime을 Long(초)으로 변환
     *
     * @param localDateTime: LocalDateTime
     * @return seconds: Long
     */
    public static Long toSeconds(LocalDateTime localDateTime) {
        return localDateTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
    }

    /**
     * Long(초)을 LocalDateTime으로 변환
     *
     * @param seconds: Long
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Long seconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds), ZoneId.systemDefault());
    }
}
