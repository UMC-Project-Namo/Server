package com.namo.spring.application.external.api.schedule.dto;

import com.namo.spring.application.external.global.annotation.validation.ValidReminderTimes;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ScheduleRequest {
    private ScheduleRequest() {
        throw new IllegalStateException("Util class");
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Schema(title = "일정 알림 수정 요청 DTO")
    public static class PutScheduleReminderDto {
        @NotNull(message = "알림을 모두 삭제할 때에 empty array를 전송합니다.")
        @ValidReminderTimes
        @Schema(description = "알림 트리거, 정시 -> 'ST', 일-> 'D{1-59 까지의 정수}', 시-> 'H{1-36 까지의 정수}', 분-> 'M{1-7 까지의 정수}'")
        private List<@NotBlank String> reminderTrigger;
    }
}
