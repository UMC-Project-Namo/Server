package com.namo.spring.application.external.api.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.user.dto.ProfileRequest;
import com.namo.spring.application.external.api.user.dto.ProfileResponse;
import com.namo.spring.application.external.api.user.usecase.ProfileUseCase;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "10. Profile", description = "내 프로필 관리 API")
@RequestMapping("/api/v2/profile")
public class ProfileController {

    private final ProfileUseCase profileUseCase;

    @GetMapping("")
    @Operation(summary = "내 프로필 보기", description = "내 프로필 정보를 조회합니다.")
    public ResponseDto<ProfileResponse.ProfileInfoDto> getProfileInfo(
            @AuthenticationPrincipal SecurityUserDetails member
    ){
        return ResponseDto.onSuccess(profileUseCase.getProfile(member.getUserId()));
    }

    @PatchMapping("")
    @Operation(summary = "내 프로필 수정", description = "내 프로필 정보를 수정합니다.")
    public ResponseDto<String> updateProfile(
            @AuthenticationPrincipal SecurityUserDetails member,
            @RequestBody ProfileRequest.UpdateProfileDto request
    ){
        profileUseCase.updateProfile(member.getUserId(), request);
        return ResponseDto.onSuccess("내 정보 수정 완료");
    }
}
