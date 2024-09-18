package com.namo.spring.application.external.api.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
        @Schema(description = "이름")
        private String name;
        @NotNull(message = "닉네임은 필수값입니다.")
        @Schema(description = "닉네임")
        private String nickname;
        @NotNull(message = "생년월일은 필수값입니다.")
        @Schema(description = "생년월일, YYYY-MM-DD 형식으로 입력합니다.", example = "2000-01-01")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") @Past
        private LocalDate birthday;
        @NotNull(message = "색상 ID는 필수 값입니다.")
        @Schema(description = "색상 ID")
        private Long colorId;
        @Schema(description = "한 줄 소개")
        private String bio;
    }
}
