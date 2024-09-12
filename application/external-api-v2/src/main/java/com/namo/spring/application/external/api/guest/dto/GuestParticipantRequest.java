package com.namo.spring.application.external.api.guest.dto;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GuestParticipantRequest {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Schema(title = "게스트 모임 가입/로그인 요청 DTO")
    public static class PostGuestParticipantDto {
        @NotBlank(message = "닉네임 입력은 필수입니다.")
        private String nickname;
        @NotBlank(message = "비밀번호 입력은 필수입니다.")
        private String password;
    }
}
