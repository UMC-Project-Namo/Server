package com.namo.spring.application.external.domain.group.ui.dto;

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
	public static class PatchGroupNameDto {
		@NotNull
		private Long groupId;
		@NotBlank
		private String groupName;
	}
}
