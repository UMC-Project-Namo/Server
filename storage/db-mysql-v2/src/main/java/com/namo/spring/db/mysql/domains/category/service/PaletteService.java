package com.namo.spring.db.mysql.domains.category.service;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.category.exception.PaletteException;
import com.namo.spring.db.mysql.domains.category.repository.PaletteRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class PaletteService {
    private final PaletteRepository paletteRepository;

    @Transactional
    public Palette createPalette(Palette palette) {
        return paletteRepository.save(palette);
    }

    @Transactional(readOnly = true)
    public Optional<Palette> readPalette(Long paletteId) {
        return paletteRepository.findById(paletteId);
    }

    @Transactional(readOnly = true)
    public Palette getPalette(Long paletteId) {
        return paletteRepository.findById(paletteId)
                .orElseThrow(() -> new PaletteException(ErrorStatus.NOT_FOUND_PALETTE_FAILURE));
    }

    @Transactional(readOnly = true)
    public List<Palette> readPaletteList(List<Long> paletteIds) {
        return paletteRepository.findByIdIn(paletteIds);
    }

}
