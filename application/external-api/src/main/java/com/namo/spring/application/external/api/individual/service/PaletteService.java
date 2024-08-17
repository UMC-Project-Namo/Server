package com.namo.spring.application.external.api.individual.service;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.PersonalException;
import com.namo.spring.db.mysql.domains.individual.domain.Palette;
import com.namo.spring.db.mysql.domains.individual.repository.palette.PaletteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaletteService {

    private final PaletteRepository paletteRepository;

    public Palette getPalette(Long paletteId) {
        return paletteRepository.findById(paletteId)
                .orElseThrow(() -> new PersonalException(ErrorStatus.NOT_FOUND_PALETTE_FAILURE));
    }

    public Palette getReferenceById(Long id) {
        return paletteRepository.getReferenceById(id);
    }
}
