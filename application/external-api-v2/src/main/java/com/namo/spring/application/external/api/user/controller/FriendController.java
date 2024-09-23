package com.namo.spring.application.external.api.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.user.usecase.FriendUseCase;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "친구", description = "친구 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
public class FriendController {

    private final FriendUseCase friendUseCase;

    @PostMapping("/{memberId}")
    public ResponseDto<String> requestFriend(
            @AuthenticationPrincipal SecurityUserDetails member,
            @Parameter(description = "친구 요청할 상대방 사용자의 ID 입니다.", example = "3")
            @PathVariable Long memberId
    ) {
        return null;
    }
}
