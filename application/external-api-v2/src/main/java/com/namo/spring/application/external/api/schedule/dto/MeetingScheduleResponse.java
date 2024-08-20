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
        @Schema(description = "모임 일정 이미지  url", example = "")
        private String imageUrl;
        @Schema(description = "모임 일정 참여자 수", example = "9")
        private Integer participantCount;
        @Schema(description = "모임 일정 참여자 이름", example = "뚜뚜, 코코아, 다나, 캐슬, 짱구, 연현, 램프, 반디, 유즈")
        private String participantNicknames;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "모임 생성 전 / 월간 일정 조회 응답 DTO")
    public static class GetMonthlyParticipantScheduleDto {
        @Schema(description = "모임 일정 ID")
        private Long scheduleId;
        @Schema(description = "일정 이름", example = "나모 정기 회의")
        private String name;
        @Schema(description = "일정 시작일, unix 타임스탬프 형식")
        private Long startDate;
        @Schema(description = "일정 종료일, unix 타임스탬프 형식")
        private Long endDate;
        @Schema(description = "시작일과 종료일 차이")
        private Long interval;
        @Schema(description = "모임 일정 참여 유저 목록")
        private List<ParticipantDto> participants;
        @Schema(description = "장소 위치 경도")
        private Double longitude;
        @Schema(description = "장소 위치 위도")
        private Double latitude;
        @Schema(description = "장소 이름", name = "스타벅스 강남역점")
        private String locationName;
        @Schema(description = "장소 카카오 맵 ID")
        private String kakaoLocationId;
        @Schema(description = "모임 일정에 대한 기록이 존재하는지 여부")
        private boolean hasDiaryPlace;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "모임 일정 조회 - 모임 일정의 참여자 목록")
    public static class ParticipantDto {
        @Schema(description = "참여자 ID")
        private Long memberId;
        @Schema(description = "닉네임")
        private String nickname;
        @Schema(description = "색상")
        private Integer color;
    }
}
