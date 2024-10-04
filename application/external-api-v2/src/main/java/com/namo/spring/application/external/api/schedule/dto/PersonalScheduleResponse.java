package com.namo.spring.application.external.api.schedule.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
        private CategoryDto categoryInfo;
        @Schema(description = "일정 시작일", example = "2024-10-04 00:00:00")
        private LocalDateTime startDate;
        @Schema(description = "일정 종료일", example = "2024-10-04 00:00:00")
        private LocalDateTime endDate;
        @Schema(description = "시작일과 종료일 차이")
        private Long interval;
        @Schema(description = "장소 정보, 없을 시 null")
        private LocationDto locationInfo;
        @Schema(description = "기록 작성 여부")
        private Boolean hasDiary;
        @Schema(description = "개인 일정 : 0, 모임 일정 : 1, 생일 일정: 2", example = "0")
        private int scheduleType;
        @Schema(description = "모임 일정 정보, 모임 일정이 아닐 시에는 null")
        private MeetingInfoDto meetingInfo;
        @Schema(description = "알림 일시, 알림 트리거, 정시 -> 'ST', 일-> 'D{1-59 까지의 정수}', 시-> 'H{1-36 까지의 정수}', 분-> 'M{1-7 까지의 정수}")
        private List<NotificationDto> notificationInfo;
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
        @Schema(description = "카테고리 공유 여부")
        private Boolean isShared;
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
        @Schema(description = "알림 트리거, 정시 -> 'ST', 일-> 'D{1-59 까지의 정수}', 시-> 'H{1-36 까지의 정수}', 분-> 'M{1-7 까지의 정수}")
        private String trigger;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "친구 일정 월간 조회 응답 DTO")
    public static class GetMonthlyFriendBirthdayDto {
        @Schema(description = "친구 이름", example = "나몽")
        private String nickname;
        @Schema(description = "친구의 생일(현재 년도 기준)", example = "2024-10-04 00:00:00")
        private LocalDateTime birthdayDate;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "친구 일정 월간 조회 응답 DTO")
    public static class GetFriendMonthlyScheduleDto {
        @Schema(description = "일정 ID")
        private Long scheduleId;
        @Schema(description = "일정 이름", example = "나모 정기 회의")
        private String title;
        @Schema(description = "카테고리 정보")
        private CategoryDto categoryInfo;
        @Schema(description = "일정 시작일", example = "2024-10-04 00:00:00")
        private LocalDateTime startDate;
        @Schema(description = "일정 종료일", example = "2024-10-04 00:00:00")
        private LocalDateTime endDate;
        @Schema(description = "시작일과 종료일 차이")
        private Long interval;
        @Schema(description = "개인 일정 : 0, 모임 일정 : 1, 친구의 생일 일정 : 2", example = "0")
        private int scheduleType;
    }

}
