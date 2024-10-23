package com.namo.spring.application.external.api.user.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Getter;

public class FriendshipResponse {

    @Getter
    @Builder
    public static class GetFriendRequestDto{
        List<FriendRequestDto> friendRequests;
        @Schema(description = "총 페이지 수", example = "5")
        private int totalPages;
        @Schema(description = "현재 페이지 번호", example = "1")
        private int currentPage;
        @Schema(description = "한 페이지당 항목 수", example = "20")
        private int pageSize;
        @Schema(description = "전체 항목 수", example = "100")
        private long totalItems;
    }

    @Getter
    @Builder
    public static class FriendRequestDto{
        @Schema(description = "친구 요청 보낸 유저 ID", example = "4")
        private Long memberId;
        @Schema(description = "친구요청 정보 ID", example = "2")
        private Long friendRequestId;
        @Schema(description = "친구 요청 유저 프로필 이미지", example = "https://static.namong.shop/resized/origin/member/image.png")
        private String profileImage;
        @Schema(description = "친구 요청 유저 닉네임", example = "캐슬")
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

    @Getter
    @Builder
    public static class GetFriendListDto{
        List<FriendInfoDto> friendList;
        @Schema(description = "총 페이지 수", example = "5")
        private int totalPages;
        @Schema(description = "현재 페이지 번호", example = "1")
        private int currentPage;
        @Schema(description = "한 페이지당 항목 수", example = "20")
        private int pageSize;
        @Schema(description = "전체 항목 수", example = "100")
        private long totalItems;
    }

    @Getter
    @Builder
    public static class FriendInfoDto{
        @Schema(description = "친구 memberID", example = "1")
        private Long memberId;
        @Schema(description = "친구 닉네임", example = "캐슬")
        private String nickname;
        @Schema(description = "친구 태그", example = "1234")
        private String tag;
        @Schema(description = "친구 한줄 소개", example = "3")
        private String bio;
        @Schema(description = "친구 생일 (비공개 존재)", example = "02-22")
        private String birthday;
        @Schema(description = "친구 선호 색", example = "2")
        private Long favoriteColorId;
    }
}
