package com.example.namo2.domain.individual.application.impl;

import static com.example.namo2.global.common.response.BaseResponseStatus.*;

import org.springframework.stereotype.Service;

import com.example.namo2.domain.individual.dao.repository.palette.PaletteRepository;
import com.example.namo2.domain.individual.domain.Palette;

import com.example.namo2.global.common.exception.BaseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaletteService {

	private final PaletteRepository paletteRepository;

	public Palette getPalette(Long paletteId) {
		return paletteRepository.findById(paletteId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_PALETTE_FAILURE));
	}

	public Palette getReferenceById(Long id) {
		return paletteRepository.getReferenceById(id);
	}
}
