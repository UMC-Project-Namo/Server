package com.namo.spring.db.mysql.domains.shop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.shop.entity.Theme;
import com.namo.spring.db.mysql.domains.shop.enums.ThemeStatus;
import com.namo.spring.db.mysql.domains.shop.enums.ThemeType;
import com.namo.spring.db.mysql.domains.shop.repository.ThemeRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;

    public Page<Theme> findByTypeAndStatus(ThemeType themeType, Pageable pageable, ThemeStatus themeStatus) {
        return themeRepository.findByTypeAndStatus(themeType, pageable, themeStatus);
    }
}
