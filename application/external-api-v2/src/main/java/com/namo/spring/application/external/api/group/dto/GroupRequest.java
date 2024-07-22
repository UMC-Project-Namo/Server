package com.namo.spring.application.external.api.group.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GroupRequest {
    private GroupRequest() {
        throw new IllegalStateException("Utils Class");
    }

    @Schema(description = "그룹 이름 수정 요청 DTO")
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateGroupNameDto {
        @Schema(description = "수정할 그룹 이름")
        @NotBlank
        private String groupName;
    }
}
