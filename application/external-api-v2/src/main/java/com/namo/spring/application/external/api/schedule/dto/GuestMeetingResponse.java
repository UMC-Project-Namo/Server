package com.namo.spring.application.external.api.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class GuestMeetingResponse {

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "월간 일정 조회 응답 DTO")
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
        private List<GuestMeetingResponse.UserParticipantDto> participants;
        @Schema(description = "현재 조회하는 모임 일정인지의 여부")
        private Boolean isCurMeetingSchedule = false;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "모임 일정 조회 - 일정의 참여자 목록")
    public static class UserParticipantDto {
        @Schema(description = "참여자 ID")
        private Long participantId;
        @Schema(description = "닉네임")
        private String nickname;
        @Schema(description = "색상 ID")
        private Long colorId;
    }
}
