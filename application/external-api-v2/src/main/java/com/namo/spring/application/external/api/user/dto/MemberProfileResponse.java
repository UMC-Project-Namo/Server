package com.namo.spring.application.external.api.user.dto;

import com.namo.spring.db.mysql.domains.user.type.FriendshipStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class MemberProfileResponse {
    private MemberProfileResponse()  {
        throw new IllegalStateException("Utility class");
    }

    @Getter
    @Builder
    public static class MemberProfileDto{
        @Schema(description = "memberID", example = "1")
        private Long memberId;
        @Schema(description = "요청 유저 프로필 이미지", example = "https://static.namong.shop/resized/origin/user/profile/image.png")
        private String profileImage;
        @Schema(description = "닉네임", example = "나몽")
        private String nickname;
        @Schema(description = "이름, 비공개 시 null", example = "이무개")
        private String name;
        @Schema(description = "태그", example = "1234")
        private String tag;
        @Schema(description = "한줄 소개", example = "3")
        private String bio;
        @Schema(description = "친구 생일, 비공개 시 null", example = "02-22")
        private String birthday;
        @Schema(description = "친구 선호 색", example = "2")
        private Long favoriteColorId;
        @Schema(description = "친구 상태", example = "ACCEPTED")
        private FriendshipStatus friendshipStatus;
    }
}
