package com.namo.spring.db.mysql.domains.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.shop.entity.Theme;
import com.namo.spring.db.mysql.domains.shop.enums.ThemeStatus;
import com.namo.spring.db.mysql.domains.shop.enums.ThemeType;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    Page<Theme> findByTypeAndStatus(ThemeType themeType, Pageable pageable, ThemeStatus themeStatus);
}
