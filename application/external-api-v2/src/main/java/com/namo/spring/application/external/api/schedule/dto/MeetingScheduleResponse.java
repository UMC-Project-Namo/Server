package com.namo.spring.application.external.api.schedule.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class MeetingScheduleResponse {
    private MeetingScheduleResponse() {
        throw new IllegalStateException("Util Class");
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "모임 일정 목록 조회 응답 DTO")
    public static class GetMeetingScheduleSummaryDto {
        @Schema(description = "모임 일정 ID", example = "1")
        private Long meetingScheduleId;
        @Schema(description = "모임 일정 제목", example = "나모 정기 회의")
        private String title;
        @Schema(description = "일정 시작일", example = "2024-10-04 00:00:00")
        private LocalDateTime startDate;
        @Schema(description = "모임 일정 이미지  url")
        private String imageUrl;
        @Schema(description = "모임 일정 참여자 수", example = "9")
        private Integer participantCount;
        @Schema(description = "모임 일정 참여자 이름", example = "뚜뚜, 코코아, 다나, 캐슬, 짱구, 연현, 램프, 반디, 유즈")
        private String participantNicknames;
        @Schema(description = "기록/활동 존재 여부")
        private boolean hasRecord;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "모임 상세 조회 응답 DTO")
    public static class GetMeetingScheduleDto {
        @Schema(description = "일정 ID")
        private Long scheduleId;
        @Schema(description = "일정 이름", example = "나모 정기 회의")
        private String title;
        @Schema(description = "모임 일정 이미지  url", example = "")
        private String imageUrl;
        @Schema(description = "일정 시작일", example = "2024-10-04 00:00:00")
        private LocalDateTime startDate;
        @Schema(description = "일정 종료일", example = "2024-10-04 00:00:00")
        private LocalDateTime endDate;
        @Schema(description = "시작일과 종료일 차이")
        private Long interval;
        @Schema(description = "위치 정보, 없을 시에는 null")
        private LocationDto locationInfo;
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
        private String title;
        @Schema(description = "일정 시작일", example = "2024-10-04 00:00:00")
        private LocalDateTime startDate;
        @Schema(description = "일정 종료일", example = "2024-10-04 00:00:00")
        private LocalDateTime endDate;
        @Schema(description = "시작일과 종료일 차이")
        private Long interval;
        @Schema(description = "일정 참여자 목록")
        private List<MemberParticipantDto> participants;
        @Schema(description = "개인 스케줄 : 0, 모임 스케줄 : 1, 생일 스케줄 : 2", example = "0")
        private int scheduleType;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "모임 생성 후 / 월간 일정 조회 응답 DTO")
    public static class GetMonthlyMeetingParticipantScheduleDto {
        @Schema(description = "일정 ID")
        private Long scheduleId;
        @Schema(description = "일정 이름", example = "나모 정기 회의")
        private String title;
        @Schema(description = "일정 시작일", example = "2024-10-04 00:00:00")
        private LocalDateTime startDate;
        @Schema(description = "일정 종료일", example = "2024-10-04 00:00:00")
        private LocalDateTime endDate;
        @Schema(description = "시작일과 종료일 차이")
        private Long interval;
        @Schema(description = "일정 참여자 목록")
        private List<UserParticipantDto> participants;
        @Schema(description = "현재 조회하는 모임 일정인지의 여부")
        private Boolean isCurMeetingSchedule = false;
        @Schema(description = "개인 일정 : 0, 모임 일정 : 1, 생일 일정 : 2", example = "0")
        private int scheduleType;
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
    @Schema(title = "모임 일정 조회 - 일정의 참여자 목록")
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
        @Schema(description = "방장 여부")
        private Boolean isOwner;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "스케줄 전체 정산 - 스케줄에 대한 모든 활동 정산 내역")
    public static class ScheduleSettlementDto{
        @Schema(description = "총 결제 금액", example = "100000")
        private BigDecimal totalAmount;
        List<SettlementUserDto> settlementUserList;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    public static class SettlementUserDto{
        @Schema(description = "닉네임#태그", example = "캐슬#1234")
        private String nickname;
        @Schema(description = "유저별 합산 금액", example = "30000")
        private BigDecimal amount;
    }
}
