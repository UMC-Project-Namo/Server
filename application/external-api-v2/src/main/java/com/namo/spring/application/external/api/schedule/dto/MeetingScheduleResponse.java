package com.namo.spring.application.external.api.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class MeetingScheduleResponse {
    private MeetingScheduleResponse() {
        throw new IllegalStateException("Util Class");
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "모임 일정 목록 조회 응답 DTO")
    public static class GetMeetingScheduleItemDto {
        @Schema(description = "모임 일정 ID", example = "1")
        private Long meetingScheduleId;
        @Schema(description = "모임 일정 제목", example = "나모 정기 회의")
        private String title;
        @Schema(description = "모임 일정 시작 일시, unix 타임 스탬프 형식")
        private Long startDate;
        @Schema(description = "모임 일정 이미지  url")
        private String imageUrl;
        @Schema(description = "모임 일정 참여자 수", example = "9")
        private Integer participantCount;
        @Schema(description = "모임 일정 참여자 이름", example = "뚜뚜, 코코아, 다나, 캐슬, 짱구, 연현, 램프, 반디, 유즈")
        private String participantNicknames;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "모임 상세 조회 응답 DTO")
    public static class GetMeetingScheduleDto {
        @Schema(description = "일정 ID")
        private Long scheduleId;
        @Schema(description = "일정 이름", example = "나모 정기 회의")
        private String name;
        @Schema(description = "모임 일정 이미지  url", example = "")
        private String imageUrl;
        @Schema(description = "일정 시작일, unix 타임스탬프 형식")
        private Long startDate;
        @Schema(description = "일정 종료일, unix 타임스탬프 형식")
        private Long endDate;
        @Schema(description = "시작일과 종료일 차이")
        private Long interval;
        @Schema(description = "위치 정보, 없을 시에는 null")
        private LocationDto location;
        @Schema(description = "일정 참여자 목록")
        private List<UserParticipantDetailDto> participants;
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
    @Schema(title = "모임 생성 전 / 월간 일정 조회 응답 DTO")
    public static class GetMonthlyMembersScheduleDto {
        @Schema(description = "일정 ID")
        private Long scheduleId;
        @Schema(description = "일정 이름", example = "나모 정기 회의")
        private String name;
        @Schema(description = "일정 시작일, unix 타임스탬프 형식")
        private Long startDate;
        @Schema(description = "일정 종료일, unix 타임스탬프 형식")
        private Long endDate;
        @Schema(description = "시작일과 종료일 차이")
        private Long interval;
        @Schema(description = "일정 참여자 목록")
        private List<MemberParticipantDto> participants;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "모임 생성 후 / 월간 일정 조회 응답 DTO")
    public static class GetMonthlyMeetingParticipantScheduleDto {
        @Schema(description = "일정 ID")
        private Long scheduleId;
        @Schema(description = "일정 이름", example = "나모 정기 회의")
        private String name;
        @Schema(description = "일정 시작일, unix 타임스탬프 형식")
        private Long startDate;
        @Schema(description = "일정 종료일, unix 타임스탬프 형식")
        private Long endDate;
        @Schema(description = "시작일과 종료일 차이")
        private Long interval;
        @Schema(description = "일정 참여자 목록")
        private List<UserParticipantDto> participants;
        @Schema(description = "현재 조회하는 모임 일정인지의 여부")
        private Boolean isCurMeetingSchedule = false;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "모임 일정 조회 - 일정의 참여자 목록")
    public static class MemberParticipantDto {
        @Schema(description = "참여자 ID")
        private Long userId;
        @Schema(description = "닉네임")
        private String nickname;
        @Schema(description = "색상 ID")
        private Long colorId;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "모임 일정 조회 - 일정의 참여자 목록, 유저 제외")
    public static class UserParticipantDto {
        @Schema(description = "참여자 ID")
        private Long participantId;
        @Schema(description = "유저 ID")
        private Long userId;
        @Schema(description = "닉네임")
        private String nickname;
        @Schema(description = "색상 ID")
        private Long colorId;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "모임 일정 조회 - 일정의 참여자 목록")
    public static class UserParticipantDetailDto {
        @Schema(description = "참여자 ID")
        private Long participantId;
        @Schema(description = "유저 ID")
        private Long userId;
        @Schema(description = "게스트 여부")
        private Boolean isGuest;
        @Schema(description = "닉네임")
        private String nickname;
        @Schema(description = "색상 ID")
        private Long colorId;
        @Schema(description = "참여 여부")
        private Boolean isActive;
        @Schema(description = "방장 여부")
        private Boolean isOwner;
    }
}
