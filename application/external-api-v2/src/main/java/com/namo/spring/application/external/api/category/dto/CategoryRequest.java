package com.namo.spring.application.external.api.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;

public class CategoryRequest {

    @Getter
    public static class CategoryCreateDto{
        @Schema(description = "생성할 카테고리 이름을 넣어주세요", example = "학교 공부")
        private String categoryName;
        @Schema(description = "카테고리에 지정할 팔레트 ID를 넣어주세요", example = "1")
        private Long colorId;
        @Schema(description = "카테고리 공개 여부를 지정해 주세요", example = "true")
        private Boolean isShared;
    }

    @Getter
    public static class CategoryUpdateDto{
        @Schema(description = "수정할 카테고리 이름을 넣어주세요", example = "자기 계발")
        private String categoryName;
        @Schema(description = "카테고리에 지정할 팔레트 ID를 넣어주세요", example = "1")
        private Long colorId;
        @Schema(description = "카테고리 공개 여부를 지정해 주세요", example = "false")
        private Boolean isShared;
    }
}
