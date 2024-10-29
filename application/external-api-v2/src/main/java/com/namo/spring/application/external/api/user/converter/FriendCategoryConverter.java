package com.namo.spring.application.external.api.user.converter;

import com.namo.spring.application.external.api.user.dto.FriendCategoryResponse;
import com.namo.spring.db.mysql.domains.category.entity.Category;

public class FriendCategoryConverter {

    public static FriendCategoryResponse.CategoryInfoDto toCategoryInfoDto(Category category){
        if (!category.isShared()){
            return null;
        }
        return FriendCategoryResponse.CategoryInfoDto.builder()
                .categoryName(category.getName())
                .colorId(category.getPalette().getId())
                .build();
    }
}
