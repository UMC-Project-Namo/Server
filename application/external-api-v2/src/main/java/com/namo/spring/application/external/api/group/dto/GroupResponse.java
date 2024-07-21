package com.namo.spring.application.external.api.group.dto;

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

    @Getter
    @AllArgsConstructor
    @Builder
    public static class GroupDto {
        private Long groupId;
        private String groupName;
        private String groupImgUrl;
        private String groupCode;
        private List<GroupUserDto> groupUsers;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GroupUserDto {
        private Long userId;
        private String userName;
        private Integer color;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GroupJoinDto {
        private Long groupId;
    }
}
