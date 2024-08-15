package com.namo.spring.application.external.api.schedule.dto;

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
    public static class PostPersonalScheduleDto {
        @NotBlank
        @Schema(description = "일정 이름", example = "알바")
        private String title;
        @NotNull
        @Schema(description = "카테고리 ID")
        private Long categoryId;
        @NotNull
        @Schema(description = "일정 시작일, unix 타임스탬프 형식")
        private Long startDate;
        @NotNull
        @Schema(description = "일정 종료일, unix 타임스탬프 형식")
        private Long endDate;
        @NotNull
        @Schema(description = "시작일과 종료일 차이")
        private Integer interval;
        @Schema(description = "장소 위치 위도")
        private Double latitude;
        @Schema(description = "장소 위치 경도")
        private Double longitude;
        @Schema(description = "장소 이름", example = "스타벅스 강남역점")
        private String locationName;
        @Schema(description = "장소 카카오 맵 ID")
        private String kakaoLocationId;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class PostMeetingScheduleDto {
        @NotBlank
        @Schema(description = "모임 일정 이름", example = "나모 정기 회의")
        private String title;
        @NotNull
        @Schema(description = "일정 시작일, unix 타임스탬프 형식")
        private Long startDate;
        @NotNull
        @Schema(description = "일정 종료일, unix 타임스탬프 형식")
        private Long endDate;
        @NotNull
        @Schema(description = "시작일과 종료일 차이")
        private Integer interval;
        @Schema(description = "장소 위치 위도")
        private Double latitude;
        @Schema(description = "장소 위치 경도")
        private Double longitude;
        @Schema(description = "장소 이름", example = "스타벅스 강남역점")
        private String locationName;
        @Schema(description = "장소 카카오 맵 ID")
        private String kakaoLocationId;
        @Schema(description = "스케줄에 참여할 유저 ID")
        private List<Long> participants;
    }
}
