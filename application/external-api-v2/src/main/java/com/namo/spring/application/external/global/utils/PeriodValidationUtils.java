package com.namo.spring.application.external.global.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.UtilsException;
import com.namo.spring.db.mysql.domains.schedule.exception.ScheduleException;
import com.namo.spring.db.mysql.domains.schedule.type.Period;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PeriodValidationUtils {

    private static final int MIN_MONTH = 1;
    private static final int MAX_MONTH = 12;
    private static final int START_OF_DAY_HOUR = 0;
    private static final int START_OF_DAY_MINUTE = 0;
    private static final int START_OF_DAY_SECOND = 0;
    private static final int END_OF_DAY_HOUR = 23;
    private static final int END_OF_DAY_MINUTE = 59;
    private static final int END_OF_DAY_SECOND = 59;

    /**
     * 시작 날짜가 끝 날짜보다 앞서지 않았는지 검증 후 기간을 반환합니다.
     *
     * @param startDate 시작 날짜
     * @param endDate   종료 날짜
     * @return 검증된 Period
     */
    public static Period getValidatedPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        Period period = Period.of(startDate, endDate);
        if (period.getStartDate().isAfter(period.getEndDate())) {
            throw new ScheduleException(ErrorStatus.INVALID_DATE);
        }
        return period;
    }

    /**
     * 시작 날짜가 끝 날짜보다 앞서지 않았는지 검증 후 반환합니다.
     *
     * @param startDate 시작 날짜
     * @param endDate   종료 날짜
     */
    public static void validatePeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new ScheduleException(ErrorStatus.INVALID_DATE);
        }
    }

    public static void validatePeriod(LocalDateTime startDate, LocalDateTime endDate){
        if (startDate.isAfter(endDate)) {
            throw new ScheduleException(ErrorStatus.INVALID_DATE);
        }
    }

    /**
     * 년도와 월이 유효한 값인지 검증합니다.
     *
     * @param year  년
     * @param month 월
     * @return 유효한 년도와 월
     */
    public static YearMonth validateYearMonth(int year, int month) {
        if (year < 0) {
            throw new UtilsException(ErrorStatus.INVALID_FORMAT_FAILURE);
        }
        if (month < MIN_MONTH || month > MAX_MONTH) {
            throw new UtilsException(ErrorStatus.INVALID_FORMAT_FAILURE);
        }
        return YearMonth.of(year, month);
    }

    /**
     * @param year  년
     * @param month 월
     * @return 입력한 달 기준 캘린더 화면의 가장 첫 날짜와, 끝 날짜의 다음 날을 반환 합니다. (전 달 및 다음 달 포함)
     */
    public static Period getExtendedPeriod(int year, int month) {
        YearMonth yearMonth = validateYearMonth(year, month);
        LocalDateTime firstDay = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime lastDay = yearMonth.atEndOfMonth().atStartOfDay();

        // 이전 달의 마지막 주 일요일로 시작 날짜 설정
        LocalDateTime startDate = firstDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        // 다음 달의 첫 주 토요일로 종료 날짜 설정
        LocalDateTime endDate = lastDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        return Period.of(startDate, endDate);
    }

    /**
     * 입력한 달 기준 1일부터 말일까지의 날을 반환합니다 (해당 달만 포함)
     * @param year
     * @param month
     * @return
     */
    public static Period getMonthPeriod(int year, int month) {
        YearMonth yearMonth = validateYearMonth(year, month); // 유효성 검사
        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay(); // 월 1일 00:00:00
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(END_OF_DAY_HOUR, END_OF_DAY_MINUTE, END_OF_DAY_SECOND); // 월 말일 23:59:59

        return Period.of(startDateTime, endDateTime);
    }

    /**
     * 입력한 날의 00:00:00~23:59:59 모든 시간이 포함되는 시간을 반환합니다.
     * @param localDate
     * @return
     */
    public static Period getPeriodForDay(LocalDate localDate) {
        LocalDateTime startDateTime = localDate.atTime(START_OF_DAY_HOUR, START_OF_DAY_MINUTE, START_OF_DAY_SECOND); // 해당 날 00:00:00
        LocalDateTime endDateTime = localDate.atTime(END_OF_DAY_HOUR, END_OF_DAY_MINUTE, END_OF_DAY_SECOND); // 해당 날 23:59:59
        return Period.of(startDateTime, endDateTime); // Period로 묶어서 반환
    }
}
