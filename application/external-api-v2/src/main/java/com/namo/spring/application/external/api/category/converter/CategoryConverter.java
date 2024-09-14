package com.namo.spring.application.external.api.category.converter;

import static com.namo.spring.db.mysql.domains.category.type.CategoryType.*;

import com.namo.spring.application.external.api.category.dto.CategoryResponse;
import com.namo.spring.db.mysql.domains.category.entity.Category;

public class CategoryConverter {

    public static CategoryResponse.MyCategoryInfoDto toMyCategoryInfoDto(Category category){
        return CategoryResponse.MyCategoryInfoDto.builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .isBaseCategory(!category.getType().equals(COMMON))
                .isShared(category.isShared())
                .paletteId(category.getPalette().getId())
                .build();
    }
}
