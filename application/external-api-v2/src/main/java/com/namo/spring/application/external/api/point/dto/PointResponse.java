package com.namo.spring.application.external.api.point.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PointResponse {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChargePointRequestListDto{
        private List<ChargePointRequestDto> chargePointRequests;
        @Schema(description = "총 페이지 수", example = "5")
        private int totalPages;
        @Schema(description = "현재 페이지 번호", example = "1")
        private int currentPage;
        @Schema(description = "한 페이지당 항목 수", example = "20")
        private int pageSize;
        @Schema(description = "전체 항목 수", example = "100")
        private long totalItems;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChargePointRequestDto {
        private String profileImage;
        private String nickname;
        private String tag;
        private Long pointTransactionId;
        private Long amount;
        private LocalDateTime requestDate;

    }
}
