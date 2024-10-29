package com.namo.spring.application.external.api.user.dto;

import lombok.Builder;
import lombok.Getter;

public class FriendCategoryResponse {

    @Getter
    @Builder
    public static class CategoryInfoDto{
        private Long colorId;
        private String categoryName;
    }
}
