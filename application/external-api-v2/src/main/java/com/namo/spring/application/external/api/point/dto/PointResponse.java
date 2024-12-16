package com.namo.spring.application.external.api.point.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PointResponse {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChargePointRequestDto {
        private String nickname;
        private String tag;
        private Long pointTransactionId;
        private Long amount;
        private LocalDateTime requestDate;

    }
}
