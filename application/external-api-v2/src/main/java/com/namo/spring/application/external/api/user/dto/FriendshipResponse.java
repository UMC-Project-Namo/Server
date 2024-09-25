package com.namo.spring.application.external.api.user.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Getter;

public class FriendshipResponse {

    @Getter
    @Builder
    public static class FriendRequestDto{
        @Schema(description = "친구 요청 보낸 유저 ID", example = "4")
        private Long memberId;
        @Schema(description = "친구요청 정보 ID", example = "2")
        private Long friendRequestId;
        @Schema(description = "친구 요청 유저 프로필 이미지", example = "https://static.namong.shop/resized/origin/member/image.png")
        private String profileImage;
        @Schema(description = "친구 요청 유저 닉네임 ID", example = "2")
        private String nickname;
        @Schema(description = "친구 요청 유저 태그", example = "1234")
        private String tag;
        @Schema(description = "친구 요청 유저 한줄 소개", example = "3")
        private String bio;
        @Schema(description = "친구 요청 유저 생일 (비공개 존재)", example = "02-22")
        private String birthday;
        @Schema(description = "친구 요청 유저 선호 색", example = "2")
        private Long favoriteColorId;
    }
}
