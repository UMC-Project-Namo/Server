package com.namo.spring.application.external.api.user.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

public class FriendshipResponse {

    @Getter
    @Builder
    public static class FriendRequestDto{
        private Long memberId;
        private String profileImage;
        private String nickname;
        private String tag;
        private String bio;
        private String birthday;
        private Long favoriteColorId;
    }
}
