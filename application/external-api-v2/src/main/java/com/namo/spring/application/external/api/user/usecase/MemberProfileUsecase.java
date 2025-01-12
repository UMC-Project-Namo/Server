package com.namo.spring.application.external.api.user.usecase;

import com.namo.spring.application.external.api.user.dto.MemberProfileResponse;
import com.namo.spring.application.external.api.user.service.MemberManageService;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.namo.spring.application.external.api.user.converter.MemberProfileConverter.toMemberProfileDto;

@Component
@RequiredArgsConstructor
public class MemberProfileUsecase {
    private final MemberManageService memberManageService;

    @Transactional(readOnly = true)
    public MemberProfileResponse.MemberProfileDto getMemberProfile(Long memberId, String nicknameTag) {
        Member target = memberManageService.getActiveMemberByNicknameTag(nicknameTag);
        return toMemberProfileDto(memberManageService.getMemberProfile(memberId, target.getId()));
    }
}
