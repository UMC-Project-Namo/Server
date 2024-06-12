package com.namo.spring.application.external.domain.individual.repository.palette;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.application.external.domain.individual.domain.Palette;

public interface PaletteRepository extends JpaRepository<Palette, Long> {
}
