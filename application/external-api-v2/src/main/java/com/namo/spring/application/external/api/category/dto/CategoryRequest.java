package com.namo.spring.application.external.api.category.dto;

import lombok.Getter;

public class CategoryRequest {

    @Getter
    public static class CategoryCreateDto{
        private String categoryName;
        private Long paletteId;
        private Boolean isShared;
    }

    @Getter
    public static class CategoryUpdateDto{
        private String categoryName;
        private Long paletteId;
        private Boolean isShared;
    }
}
