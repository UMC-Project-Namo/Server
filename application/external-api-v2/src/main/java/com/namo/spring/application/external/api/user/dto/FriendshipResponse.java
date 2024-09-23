package com.namo.spring.application.external.api.user.dto;

import lombok.Builder;
import lombok.Getter;

public class FriendshipResponse {

    @Getter
    @Builder
    public static class FriendRequestDto{
        private String profileImage;
        private String nickname;
        private String tag;
        private String bio;
    }
}
