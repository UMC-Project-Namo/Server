package com.namo.spring.application.external.api.group.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class GroupResponse {
    private GroupResponse() {
        throw new IllegalStateException("Util Class");
    }

    @AllArgsConstructor
    @Getter
    @Builder
    public static class GroupIdDto {
        private Long groupId;
    }

    @Schema(description = "그룹 목록 조회 응답 DTO")
    @Getter
    @AllArgsConstructor
    @Builder
    public static class GroupDto {
        @Schema(description = "그룹 ID")
        private Long groupId;
        @Schema(description = "그룹 이름")
        private String groupName;
        @Schema(description = "그룹 프로필 이미지")
        private String groupImgUrl;
        @Schema(description = "그룹 참여 코드")
        private String groupCode;
        @Schema(description = "그룹 참여자 목록")
        private List<GroupUserDto> groupUsers;
    }

    @Schema(description = "그룹 참여자 조회 응답 DTO")
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GroupUserDto {
        @Schema(description = "유저 ID")
        private Long userId;
        @Schema(description = "유저 이름")
        private String userName;
        @Schema(description = "유저 색상")
        private Integer color;
    }

    @Schema(description = "그룹 참여 응답 DTO")
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GroupJoinDto {
        @Schema(description = "그룹 ID")
        private Long groupId;
    }
}
