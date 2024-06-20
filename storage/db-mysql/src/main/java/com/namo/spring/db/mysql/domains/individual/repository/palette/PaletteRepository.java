package com.namo.spring.db.mysql.domains.individual.repository.palette;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.individual.domain.Palette;

public interface PaletteRepository extends JpaRepository<Palette, Long> {
}
