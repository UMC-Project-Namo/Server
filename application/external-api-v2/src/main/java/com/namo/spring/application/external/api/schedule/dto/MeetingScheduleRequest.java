package com.namo.spring.application.external.api.schedule.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MeetingScheduleRequest {

    private MeetingScheduleRequest() {
        throw new IllegalStateException("Util class");
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Schema(title = "모임 일정 생성 요청 DTO")
    public static class PostMeetingScheduleDto {
        @NotBlank(message = "일정 이름은 공백일 수 없습니다.")
        @Schema(description = "모임 일정 이름", example = "나모 정기 회의")
        private String title;
        @Schema(description = "이미지 URL, null을 입력하면 기본 이미지가 생성됩니다.", example = "https://static.namong.shop/origin/diary/image.jpg")
        private String imageUrl;
        @NotNull(message = "일정 시작일, 종료일 정보는 필수 입니다.")
        @Schema(description = "기간 정보")
        private PeriodDto period;
        @NotNull(message = "장소가 없을 시 emtpy object를 전송합니다.")
        @Schema(description = "카카오 맵 장소 정보, 장소가 없을 시 emtpy object를 전송합니다.")
        private LocationDto location;
        @NotNull(message = "모임 일정에 참여할 친구는 1명부터 9명까지 입력 가능합니다.")
        @Schema(description = "스케줄에 참여할 유저 ID")
        private List<Long> participants;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class PeriodDto {
        @NotNull(message = "일정 시작일은 필수입니다.")
        @Schema(description = "일정 시작일, unix 타임스탬프 형식")
        private Long startDate;
        @NotNull(message = "일정 종료일은 필수입니다.")
        @Schema(description = "일정 종료일, unix 타임스탬프 형식")
        private Long endDate;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class LocationDto {
        @Schema(description = "카카오맵 좌표계 상의 x 좌표")
        private Double longitude;
        @Schema(description = "카카오맵 좌표계 상의 y 좌표")
        private Double latitude;
        @Schema(description = "장소 이름", example = "스타벅스 강남역점")
        private String locationName;
        @Schema(description = "장소 카카오 맵 ID")
        private String kakaoLocationId;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Schema(title = "모임 일정 수정 요청 DTO")
    public static class PatchMeetingScheduleDto {
        @NotBlank(message = "일정 이름 입력은 필수 입니다. 수정 사항이 없을 시 원본 값을 전송합니다.")
        @Schema(description = "모임 일정 이름, 수정 사항이 없을 시 원본 값을 전송합니다.")
        private String title;
        @NotBlank(message = "이미지 url 입력은 필수 입니다. 수정 사항이 없을 시 원본 값을 전송합니다.")
        @Schema(description = "이미지 URL", example = "https://static.namong.shop/origin/diary/image.jpg")
        private String imageUrl;
        @NotNull(message = "기간 입력은 필수 입니다. 수정 사항이 없을 시 원본 값을 전송합니다.")
        @Schema(description = "기간, 수정 사항이 없을 시 원본 값을 전송합니다.")
        private PeriodDto period;
        @NotNull(message = "수정 사항이 없을 경우 empty object를 전송합니다.")
        @Schema(description = "카카오 맵 장소 정보, 수정 사항이 없을 시 원본 값을 전송합니다.(원래 값이 없을 경우 empty object)")
        private LocationDto location;
        @NotNull(message = "추가할 유저가 없을 시 empty array를 전송하세요.")
        @Schema(description = "스케줄에 추가할 유저 ID(userId), 추가할 유저가 없을 시 empty array를 전송합니다.")
        private List<Long> participantsToAdd;
        @NotNull(message = "삭제할 유저가 없을 시 empty array를 전송하세요.")
        @Schema(description = "스케줄에서 삭제할 참가자 ID(participantId), 삭제할 유저가 없을 시 empty array를 전송합니다.")
        private List<Long> participantsToRemove;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Schema(title = "모임 일정 프로필 수정 요청 DTO")
    public static class PatchMeetingScheduleProfileDto {
        @NotBlank(message = "일정 이름 입력은 필수 입니다. 수정 사항이 없을 시 원본 값을 전송합니다.")
        @Schema(description = "모임 일정 이름, 수정 사항이 없을 시 원본 값을 전송합니다.")
        private String title;
        @NotBlank(message = "이미지 url 입력은 필수 입니다. 수정 사항이 없을 시 원본 값을 전송합니다.")
        @Schema(description = "이미지 URL", example = "https://static.namong.shop/origin/diary/image.jpg")
        private String imageUrl;
    }
}
