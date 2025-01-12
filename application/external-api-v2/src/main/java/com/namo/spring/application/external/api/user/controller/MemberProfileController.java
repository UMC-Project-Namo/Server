package com.namo.spring.application.external.api.user.controller;

import com.namo.spring.application.external.api.user.dto.MemberProfileResponse;
import com.namo.spring.application.external.api.user.usecase.MemberProfileUsecase;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "11. Profile", description = "유저 프로필 API")
@RequestMapping("/api/v2/members")
public class MemberProfileController {
    private final MemberProfileUsecase memberProfileUsecase;

    @Operation(summary = "유저 프로필 및 친구 상태 조회", description = "유저 프로필 및 친구 상태를 조회합니다.")
    @GetMapping("/{nickname-tag}/profile")
    public ResponseDto<MemberProfileResponse.MemberProfileDto> getMemberProfile(
            @AuthenticationPrincipal SecurityUserDetails member,
            @PathVariable("nickname-tag") String nicknameTag){
        return ResponseDto.onSuccess(memberProfileUsecase.getMemberProfile(member.getUserId(), nicknameTag));
    }
}
