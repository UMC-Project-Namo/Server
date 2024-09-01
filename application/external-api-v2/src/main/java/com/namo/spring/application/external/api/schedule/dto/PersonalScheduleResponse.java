package com.namo.spring.application.external.api.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class PersonalScheduleResponse {
    private PersonalScheduleResponse() {
        throw new IllegalStateException("Util Class");
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "개인 일정 월간 조회 응답 DTO")
    public static class GetMonthlyScheduleDto {
        @Schema(description = "일정 ID")
        private Long scheduleId;
        @Schema(description = "일정 이름", example = "나모 정기 회의")
        private String title;
        @Schema(description = "카테고리 정보")
        private CategoryDto category;
        @Schema(description = "일정 시작일, unix 타임스탬프 형식")
        private Long startDate;
        @Schema(description = "일정 종료일, unix 타임스탬프 형식")
        private Long endDate;
        @Schema(description = "시작일과 종료일 차이")
        private Long interval;
        @Schema(description = "장소 정보, 없을 시 null")
        private LocationDto location;
        @Schema(description = "기록 작성 여부")
        private Boolean hasDiary;
        @Schema(description = "모임 일정인지의 여부")
        private Boolean isMeetingSchedule = false;
        @Schema(description = "모임 일정 정보, 모임 일정이 아닐 시에는 null")
        private MeetingInfoDto meetingInfo;
        @Schema(description = "알림 일시, 없을 시 null")
        private List<NotificationDto> notification;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    public static class MeetingInfoDto {
        @Schema(description = "모임 인원 수")
        private Integer participantCount;
        @Schema(description = "모임 참여자들 닉네임")
        private String participantNicknames;
        @Schema(description = "모임 생성자 여부")
        private Boolean isOwner;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    public static class CategoryDto {
        @Schema(description = "카테고리 ID")
        private Long categoryId;
        @Schema(description = "색상 ID")
        private Long colorId;
        @Schema(description = "카테고리 이름")
        private String name;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    public static class LocationDto {
        @Schema(description = "장소 위치 경도")
        private Double longitude;
        @Schema(description = "장소 위치 위도")
        private Double latitude;
        @Schema(description = "장소 이름", name = "스타벅스 강남역점")
        private String locationName;
        @Schema(description = "장소 카카오 맵 ID")
        private String kakaoLocationId;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    public static class NotificationDto {
        @Schema(description = "알림 ID")
        private Long notificationId;
        @Schema(description = "알림 일시, unix 타임스탬프 형식")
        private Long notifyDate;
    }

}
