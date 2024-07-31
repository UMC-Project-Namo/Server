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
    @Schema(title = "그룹 생성 응답 DTO")
    public static class GroupIdDto {
        @Schema(description = "그룹 ID")
        private Long groupId;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @Schema(title = "그룹 목록 조회 응답 DTO")
    public static class GetGroupDto {
        @Schema(description = "그룹 ID")
        private Long groupId;
        @Schema(description = "그룹명")
        private String groupName;
        @Schema(description = "그룹 프로필 이미지 url")
        private String groupImgUrl;
        @Schema(description = "그룹 참여 코드")
        private String groupCode;
        @Schema(description = "그룹 구성원 목록")
        private List<GetGroupUserDto> groupUsers;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Schema(title = "그룹 목록 조회 응답 - 그룹의 구성원")
    public static class GetGroupUserDto {
        @Schema(description = "유저 ID")
        private Long userId;
        @Schema(description = "유저 이름")
        private String userName;
        @Schema(description = "유저 색상")
        private Integer color;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Schema(title = "그룹 참여 응답 DTO")
    public static class GroupParticipantDto {
        @Schema(description = "그룹 ID")
        private Long groupId;
        @Schema(description = "그룹 참여 코드")
        private String code;
    }
}
