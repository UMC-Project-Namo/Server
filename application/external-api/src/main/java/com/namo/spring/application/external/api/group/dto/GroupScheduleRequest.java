package com.namo.spring.application.external.api.group.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * v1
 */
public class GroupScheduleRequest {
    private GroupScheduleRequest() {
        throw new IllegalStateException("Util class");
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Schema(title = "모임 일정 생성 요청 DTO")
    public static class PostGroupScheduleDto {
        @NotNull
        @Schema(description = "그룹 ID")
        private Long groupId;
        @NotBlank
        @Schema(description = "모임 일정 이름", example = "나모 정기 회의")
        private String name;
        @NotNull
        @Schema(description = "일정 시작일, unix 타임스탬프 형식")
        private Long startDate;
        @NotNull
        @Schema(description = "일정 종료일, unix 타임스탬프 형식")
        private Long endDate;
        @NotNull
        @Schema(description = "시작일과 종료일 차이")
        private Integer interval;
        @Schema(description = "장소 위치 경도")
        private Double x;
        @Schema(description = "장소 위치 위도")
        private Double y;
        @Schema(description = "장소 이름", example = "스타벅스 강남역점")
        private String locationName;
        @Schema(description = "장소 카카오 맵 ID")
        private String kakaoLocationId;
        @NotNull
        @Schema(description = "일정에 참여할 유저 ID 목록")
        private List<Long> users;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Schema(title = "모임 일정 수정 요청 DTO")
    public static class PatchGroupScheduleDto {
        @NotNull
        @Schema(description = "모임 일정 ID")
        private Long moimScheduleId;
        @NotBlank
        @Schema(description = "모임 일정 이름", example = "나모 정기 회의")
        private String name;
        @NotNull
        @Schema(description = "일정 시작일, unix 타임스탬프 형식")
        private Long startDate;
        @NotNull
        @Schema(description = "일정 종료일, unix 타임스탬프 형식")
        private Long endDate;
        @NotNull
        @Schema(description = "시작일과 종료일 차이")
        private Integer interval;
        @Schema(description = "장소 위치 경도")
        private Double x;
        @Schema(description = "장소 위치 위도")
        private Double y;
        @Schema(description = "장소 이름", name = "스타벅스 가천대역점")
        private String locationName;
        @Schema(description = "장소 카카오 맵 ID")
        private String kakaoLocationId;
        @NotNull
        @Schema(description = "일정에 참여할 유저 ID 목록")
        private List<Long> users;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Schema(title = "모임 일정 카테고리 수정 요청 DTO")
    public static class PatchGroupScheduleCategoryDto {
        @NotNull
        @Schema(description = "모임 일정 ID")
        private Long moimScheduleId;
        @NotNull
        @Schema(description = "카테고리 ID")
        private Long categoryId;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Schema(title = "모임 일정 알림 생성 요청 DTO")
    public static class PostGroupScheduleAlarmDto {
        @Schema(description = "모임 일정 ID")
        private Long moimScheduleId;
        @Schema(description = "알림이 전송될 시각 목록")
        private List<Integer> alarmDates;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Schema(title = "모임 일정 알림 수정 요청 DTO")
    public static class PatchGroupScheduleAlarmDto {
        @Schema(description = "모임 일정 ID")
        private Long moimScheduleId;
        @Schema(description = "알림이 전송될 시각 목록")
        private List<Integer> alarmDates;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostGroupScheduleTextDto {
        private String text;
    }
}
