package com.namo.spring.application.external.api.record.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ActivityResponse {

    @Builder
    @Getter
    @AllArgsConstructor
    @Schema(description = "활동 조회 DTO")
    public static class ActivityInfoDto {
        private Long activityId;
        private String activityTitle;
        private List<ActivityParticipantDto> activityParticipants;
        private LocalDateTime activityStartDate;
        private LocalDateTime activityEndDate;
        private ActivityLocationDto activityLocation;
        private BigDecimal totalAmount;
        private String tag;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @Schema(description = "활동 참여자 DTO")
    public static class ActivityParticipantDto {
        private Long participantMemberId;
        private String participantNickname;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @Schema(description = "활동 장소 DTO")
    public static class ActivityLocationDto {
        @Schema(description = "카카오맵 좌표계 상의 x 좌표")
        private Double longitude;
        @Schema(description = "카카오맵 좌표계 상의 y 좌표")
        private Double latitude;
        @Schema(description = "장소 이름", example = "스타벅스 강남역점")
        private String locationName;
        @Schema(description = "장소 카카오 맵 ID")
        private String kakaoLocationId;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ActivitySettlementInfoDto {
        private BigDecimal totalAmount;
        private int divisionCount;
        private BigDecimal amountPerPerson;
        private List<ActivitySettlementParticipant> participants;

    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ActivitySettlementParticipant {
        private Long activityParticipantId;
        private String participantNickname;
        private boolean isIncludedInSettlement;
    }
}
