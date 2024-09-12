package com.namo.spring.application.external.api.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class MemberResponse {

    private MemberResponse() {
        throw new IllegalStateException("Utility class");
    }

    @Getter
    @Builder
    public static class SignUpDto {
        private String accessToken;
        private String refreshToken;
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
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Builder
    public static class SignUpDoneDto {
        private String nickname;
        private String tag;
        private String name;
        private String bio;
        private String birth;
        private Long colorId;
    }
}
