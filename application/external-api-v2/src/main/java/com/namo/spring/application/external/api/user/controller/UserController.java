package com.namo.spring.application.external.api.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "1. 유저 정보", description = "유저 정보 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/users")
public class UserController {

    @Operation(summary = "선호 색상 수정", description = "나의 선호 색상을 수정합니다.")
    @PostMapping(value = "/edit/color")
    public ResponseDto<String> updateColor(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @RequestParam Long colorId
    ){
        return ResponseDto.onSuccess("선호 색상 수정 완료");
    }

}
