package com.namo.spring.application.external.api.group.dto;

import jakarta.validation.constraints.NotBlank;
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
    public static class UpdateGroupNameDto {
        @NotBlank
        private String groupName;
    }
}
