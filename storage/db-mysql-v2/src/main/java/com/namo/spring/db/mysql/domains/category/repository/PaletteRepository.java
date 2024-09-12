package com.namo.spring.db.mysql.domains.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.category.entity.Palette;

public interface PaletteRepository extends JpaRepository<Palette, Long> {
    List<Palette> findByIdIn(List<Long> paletteIds);
}
