package com.namo.spring.application.external.api.schedule.dto;

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
        private Integer participantsNum;
        @Schema(description = "모임 일정 참여자 이름", example = "뚜뚜,코코아,다나,캐슬,짱구,연현,램프,반디,유즈")
        private String participantsNickname;
    }

}
