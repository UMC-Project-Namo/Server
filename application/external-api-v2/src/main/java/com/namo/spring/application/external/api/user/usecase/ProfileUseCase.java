package com.namo.spring.application.external.api.user.usecase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.user.converter.ProfileConverter;
import com.namo.spring.application.external.api.user.dto.ProfileRequest;
import com.namo.spring.application.external.api.user.dto.ProfileResponse;
import com.namo.spring.application.external.api.user.service.MemberManageService;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.category.service.PaletteService;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProfileUseCase {

    private final MemberManageService memberManageService;
    private final PaletteService paletteService;

    @Transactional(readOnly = true)
    public ProfileResponse.ProfileInfoDto getProfile(Long memberId) {
        Member member = memberManageService.getActiveMember(memberId);
        return ProfileConverter.toProfileInfoDto(member);
    }

    @Transactional
    public void updateProfile(Long memberId, ProfileRequest.UpdateProfileDto request) {
        Member member = memberManageService.getActiveMember(memberId);
        Palette palette = paletteService.getPalette(request.getFavoriteColorId());
        member.updatePalette(palette);
        member.updateProfile(request.getNickname(), request.isNameVisible(),request.getBirthday(),
                request.isBirthdayVisible(), request.getBio(), request.getProfileImage());
        memberManageService.saveMember(member);
    }
}
