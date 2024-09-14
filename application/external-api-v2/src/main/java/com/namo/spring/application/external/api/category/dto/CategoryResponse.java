package com.namo.spring.application.external.api.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class CategoryResponse {

    @Builder
    @Getter
    @AllArgsConstructor
    @Schema(description = "카테고리 조회 DTO")
    public static class MyCategoryInfoDto{
        private Long categoryId;
        private String categoryName;
        private Long paletteId;
        private boolean isBaseCategory;
        private boolean isVisible;
    }
}
