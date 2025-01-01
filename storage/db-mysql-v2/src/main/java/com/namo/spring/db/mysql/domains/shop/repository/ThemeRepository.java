package com.namo.spring.db.mysql.domains.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.shop.entity.Theme;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
}
