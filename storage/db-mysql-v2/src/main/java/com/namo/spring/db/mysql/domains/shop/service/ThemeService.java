package com.namo.spring.db.mysql.domains.shop.service;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.shop.repository.ThemeRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;

}
