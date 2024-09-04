package com.namo.spring.application.external.global.annotation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 알림 시간 목록의 유효성을 검사합니다.
 * 이 어노테이션은 List<String> 타입의 필드에 적용될 수 있습니다.
 * 유효한 알림 시간 형식은 다음과 같습니다:
 * - M1 ~ M59: 1분부터 59분
 * - H1 ~ H36: 1시간부터 36시간
 * - D1 ~ D7: 1일부터 7일
 */
@Documented
@Constraint(validatedBy = ReminderTimesValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidReminderTimes {
    String message() default "알림 시간 형식이 올바르지 않습니다.";

    /**
     * 유효성 검사 그룹을 지정하며,
     * 기본값은 빈 배열입니다.
     *
     * @return 유효성 검사 그룹 클래스 배열
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
