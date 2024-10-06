package com.namo.spring.application.external.api.user.usecase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.user.service.MemberManageService;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.category.service.PaletteService;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserUseCase {

    private final MemberManageService memberManageService;
    private final PaletteService paletteService;

    @Transactional
    public void updatePreferenceColor(Long memberId, Long colorId) {
        Member target = memberManageService.getActiveMember(memberId);
        Palette palette = paletteService.getPalette(colorId);
        target.updatePalette(palette);
    }
}
