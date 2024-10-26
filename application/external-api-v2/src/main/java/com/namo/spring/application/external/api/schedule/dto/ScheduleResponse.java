package com.namo.spring.application.external.api.schedule.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ScheduleResponse {

    private ScheduleResponse() {
        throw new IllegalStateException("Util Class");
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ScheduleSummaryDto {
        @Schema(description = "스케줄 ID 입니다.", example = "2")
        private Long scheduleId;
        @Schema(description = "스케줄 제목입니다.", example = "코딩스터디")
        private String scheduleTitle;
        @Schema(description = "스케줄 시작 날짜입니다.")
        private LocalDateTime scheduleStartDate;
        @Schema(description = "스케줄 종료 날짜입니다.")
        private LocalDateTime scheduleEndDate;
        private LocationInfoDto locationInfo;
        private CategoryInfoDto categoryInfo;
        private List<ParticipantInfoDto> participantInfo;
        private int participantCount;
        private boolean hasDiary;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class LocationInfoDto {
        @Schema(description = "카카오 장소 id입니다.", example = "26338954")
        private String kakaoLocationId;
        @Schema(description = "장소 이름입니다.", example = "탐앤탐스 탐스커버리 건대점")
        private String locationName;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @Schema(description = "카테고리 정보 DTO")
    public static class CategoryInfoDto {
        @Schema(description = "카테고리 이름", example = "개인 일정")
        private String name;
        @Schema(description = "카테고리 색상", example = "1")
        private Long colorId;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @Schema(description = "참여자 정보 DTO")
    public static class ParticipantInfoDto{
        @Schema(description = "유저 ID (회원일 경우 memberId, 비회원인 경우 AnonymousId)", example = "2")
        private Long userId;
        @Schema(description = "스케줄 참여자 ID", example = "23")
        private Long participantId;
        @Schema(description = "유저 이름", example = "캐슬")
        private String nickname;
        @Schema(description = "게스트 여부 (true: 비회원/ false: 회원) ", example = "true")
        private Boolean isGuest;
    }

}
