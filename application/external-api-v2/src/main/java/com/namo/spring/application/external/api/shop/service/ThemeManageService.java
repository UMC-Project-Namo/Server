package com.namo.spring.application.external.api.shop.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.shop.entity.Theme;
import com.namo.spring.db.mysql.domains.shop.enums.ThemeStatus;
import com.namo.spring.db.mysql.domains.shop.enums.ThemeType;
import com.namo.spring.db.mysql.domains.shop.exceptions.ThemeException;
import com.namo.spring.db.mysql.domains.shop.service.MemberThemeService;
import com.namo.spring.db.mysql.domains.shop.service.ThemeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ThemeManageService {

    private final ThemeService themeService;
    private final MemberThemeService memberThemeService;

    public Page<Theme> getSellingThemesByType(ThemeType themeType, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return themeService.findByTypeAndStatus(themeType, pageable, ThemeStatus.SELLING);
    }

    public Theme getThemeByIdWithOwnership(Long memberId, Long themeId) {
        Theme theme = themeService.findByIdAndStatus(themeId, ThemeStatus.SELLING)
                .orElseThrow(() -> new ThemeException(ErrorStatus.NOT_FOUND_THEME));
        boolean isOwned = memberThemeService.purchaseInfo(memberId, themeId);
        theme.setOwned(isOwned);
        return theme;
    }
}
