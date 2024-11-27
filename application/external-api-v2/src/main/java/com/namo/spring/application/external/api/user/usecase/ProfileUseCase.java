package com.namo.spring.application.external.api.user.usecase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.user.converter.ProfileConverter;
import com.namo.spring.application.external.api.user.dto.ProfileResponse;
import com.namo.spring.application.external.api.user.service.MemberManageService;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProfileUseCase {

    private MemberManageService memberManageService;

    @Transactional(readOnly = true)
    public ProfileResponse.ProfileInfoDto getProfile(Long memberId) {
        Member member = memberManageService.getActiveMember(memberId);
        return ProfileConverter.toProfileInfoDto(member);
    }
}
