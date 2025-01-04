package com.namo.spring.application.external.api.shop.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.namo.spring.application.external.api.shop.dto.ThemeResponse;
import com.namo.spring.db.mysql.domains.shop.entity.Theme;

public class ThemeConverter {

    public static ThemeResponse.ThemeDtoList toThemeDtoList(Page<Theme> themes) {
        List<ThemeResponse.ThemePreviewDto> themeDtoList = themes.stream()
                .map(ThemeConverter::toThemeResponseDto)
                .collect(Collectors.toList());

        return ThemeResponse.ThemeDtoList.builder()
                .themes(themeDtoList)
                .totalPages(themes.getTotalPages())
                .currentPage(themes.getNumber() + 1)
                .pageSize(themes.getSize())
                .totalItems(themes.getTotalElements())
                .build();
    }

    public static ThemeResponse.ThemePreviewDto toThemeResponseDto(Theme theme) {
        return ThemeResponse.ThemePreviewDto.builder()
            .id(theme.getId())
            .name(theme.getName())
            .description(theme.getDescription())
            .price(theme.getPrice())
            .previewImageUrl(theme.getPreviewImageUrl())
            .build();
    }
}
