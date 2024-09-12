package com.namo.spring.application.external.api.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberRequest {
    private MemberRequest() {
        throw new IllegalStateException("Utility class");
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LogoutDto {
        @NotBlank
        private String accessToken;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpDto {
        @NotBlank
        private String accessToken;
        @NotBlank
        private String refreshToken;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SocialSignUpDto {
        @NotBlank
        private String accessToken;
        @NotBlank
        private String socialRefreshToken;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppleSignUpDto {
        @NotBlank
        private String authorizationCode;
        @NotBlank
        private String identityToken;
    }

    @NoArgsConstructor
    public static class TermDto {
        @NotNull
        private boolean isCheckTermOfUse;
        @NotNull
        private boolean isCheckPersonalInformationCollection;

        public boolean getIsCheckTermOfUse() {
            return isCheckTermOfUse;
        }

        public boolean getIsCheckPersonalInformationCollection() {
            return isCheckPersonalInformationCollection;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompleteSignUpDto {
        @NotNull(message = "이름은 필수값입니다.")
        private String name;
        @NotNull(message = "닉네임은 필수값입니다.")
        private String nickname;
        @NotNull(message = "생년월일은 필수값입니다.")
        private String birthday;
        @NotNull(message = "색상 ID는 필수 값입니다.")
        private Long colorId;
        private String bio;
    }
}
