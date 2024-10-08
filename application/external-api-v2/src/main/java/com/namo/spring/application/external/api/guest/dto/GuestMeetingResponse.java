package com.namo.spring.application.external.api.guest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class GuestMeetingResponse {

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "모임 일정 조회 응답 DTO")
    public static class GetMeetingScheduleInfoDto {
        @Schema(description = "일정 ID")
        private Long scheduleId;
        @Schema(description = "일정 이름", example = "나모 정기 회의")
        private String title;
        @Schema(description = "일정 시작일")
        private LocalDateTime startDate;
        @Schema(description = "모임 이미지")
        private String imageUrl;
    }

}
