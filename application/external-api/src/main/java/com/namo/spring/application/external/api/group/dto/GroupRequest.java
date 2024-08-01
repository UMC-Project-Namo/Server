package com.namo.spring.application.external.api.group.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GroupRequest {
    private GroupRequest() {
        throw new IllegalStateException("Utils Class");
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(title = "그룹 이름 수정 요청 DTO")
    public static class PatchGroupNameDto {
        @NotNull
        @Schema(description = "그룹 ID")
        private Long groupId;
        @NotBlank
        @Schema(description = "그룹명")
        private String groupName;
    }
}
