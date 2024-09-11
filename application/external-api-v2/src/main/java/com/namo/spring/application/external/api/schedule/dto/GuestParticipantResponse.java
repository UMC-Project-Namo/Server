package com.namo.spring.application.external.api.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class GuestParticipantResponse {

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "게스트 모임 가입/로그인 응답 DTO")
    public static class PostGuestParticipantDto {
        private Long userId;
        private Long participantId;
        private Long scheduleId;
    }
}
