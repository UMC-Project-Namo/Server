package com.namo.spring.application.external.api.user.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProfileRequest {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateProfileDto{
        @NotNull(message = "닉네임은 필수 값입니다.")
        private String nickname;
        @NotNull(message = "이름 공개 여부는 필수 값입니다.")
        private boolean isNameVisible;
        @NotNull(message = "생일은 필수 값입니다.")
        private LocalDate birthday;
        @NotNull(message = "생일 공개 여부는 필수 값입니다.")
        private boolean isBirthdayVisible;
        private String bio;
        private String profileImage;
        @NotNull(message = "선호 색상은 필수 값입니다.")
        private Long favoriteColorId;
    }
}
