package com.namo.spring.application.external.global.utils;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.UtilsException;
import com.namo.spring.db.mysql.domains.schedule.exception.ScheduleException;
import com.namo.spring.db.mysql.domains.schedule.type.Period;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;

@Component
@RequiredArgsConstructor
public class SchedulePeriodValidationUtils {

    /**
     * 시작 날짜가 끝 날짜보다 앞서지 않았는지 검증 후 반환합니다.
     *
     * @param startDate 시작 날짜
     * @param endDate   종료 날짜
     * @return 검증된 Period
     */
    public static Period getValidatedPeriod(Long startDate, Long endDate) {
        Period period = Period.of(startDate, endDate);
        if (period.getStartDate().isAfter(period.getEndDate())) {
            throw new ScheduleException(ErrorStatus.INVALID_DATE);
        }
        return period;
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
        if (month < 1 || month > 12) {
            throw new UtilsException(ErrorStatus.INVALID_FORMAT_FAILURE);
        }
        return YearMonth.of(year, month);
    }

    /**
     * @param year  년
     * @param month 월
     * @return 입력한 달 기준 캘린더 화면의 가장 첫 날짜와, 끝 날짜를 반환 합니다. (전 달 및 다음 달 포함)
     */

    public static Period getExtendedPeriod(int year, int month) {
        YearMonth yearMonth = validateYearMonth(year, month);
        LocalDateTime firstDay = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime lastDay = yearMonth.atEndOfMonth().atStartOfDay();

        // 이전 달의 마지막 주 일요일로 시작 날짜 설정
        LocalDateTime startDate = firstDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        // 다음 달의 첫 주 토요일로 종료 날짜 설정
        LocalDateTime endDate = lastDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        return Period.of(startDate, endDate);
    }
}
