package com.namo.spring.application.external.api.schedule.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.namo.spring.application.external.global.annotation.validation.ValidReminderTimes;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PersonalScheduleRequest {

    private PersonalScheduleRequest() {
        throw new IllegalStateException("Util class");
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Schema(title = "개인 일정 생성 요청 DTO")
    public static class PostPersonalScheduleDto {
        @NotBlank(message = "일정 이름은 공백일 수 없습니다.")
        @Schema(description = "일정 이름", example = "알바")
        private String title;
        @NotNull(message = "카테고리 ID는 필수입니다.")
        @Schema(description = "카테고리 ID")
        private Long categoryId;
        @NotNull(message = "일정 시작일, 종료일 정보는 필수 입니다.")
        @Schema(description = "기간 정보")
        private MeetingScheduleRequest.PeriodDto period;
        @NotNull(message = "장소 정보가 없을 경우 empty object를 전송합니다.")
        @Schema(description = "카카오 맵 장소 정보")
        private MeetingScheduleRequest.LocationDto location;
        @NotNull(message = "알림이 없을 시 emtpy array를 전송합니다.")
        @ValidReminderTimes
        @Schema(description = "알림 트리거, 정시 -> 'ST', 일-> 'D{1-59 까지의 정수}', 시-> 'H{1-36 까지의 정수}', 분-> 'M{1-7 까지의 정수}'")
        private List<@NotBlank String> reminderTrigger;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Schema(title = "개인 일정 수정 요청 DTO")
    public static class PatchPersonalScheduleDto {
        @NotBlank(message = "일정 이름 입력은 필수 입니다. 수정 사항이 없을 시 원본 값을 전송합니다.")
        @Schema(description = "개인 일정 이름, 수정 사항이 없을 시 원본 값을 전송합니다.")
        private String title;
        @NotNull(message = "기간 입력은 필수 입니다. 수정 사항이 없을 시 원본 값을 전송합니다.")
        @Schema(description = "기간, 수정 사항이 없을 시 원본 값을 전송합니다.")
        private MeetingScheduleRequest.PeriodDto period;
        @NotNull(message = "수정 사항이 없을 경우 empty object를 전송합니다.")
        @Schema(description = "카카오 맵 장소 정보, 수정 사항이 없을 시 원본 값을 전송합니다.(원래 값이 없을 경우 empty object)")
        private MeetingScheduleRequest.LocationDto location;
    }
}
