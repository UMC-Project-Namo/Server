package com.namo.spring.application.external.api.user.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

public class MemberResponse {

    private MemberResponse() {
        throw new IllegalStateException("Utility class");
    }

    @Getter
    @Builder
    public static class SignUpDto {
        private String accessToken;
        private String refreshToken;
        private Long userId;
        private boolean newUser;
        private boolean signUpComplete;
        private List<TermsDto> terms;
    }

    @Getter
    @Builder
    public static class TermsDto {
        private String content;
        private boolean isCheck;
    }

    @Getter
    @Builder
    public static class ReissueDto {
        private Long userId;
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Builder
    public static class SignUpDoneDto {
        private Long userId;
        private String nickname;
        private String tag;
        private String name;
        private String bio;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate birthday;
        private Long colorId;
    }
}
