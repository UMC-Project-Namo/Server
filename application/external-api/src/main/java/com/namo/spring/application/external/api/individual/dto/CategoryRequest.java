package com.namo.spring.application.external.api.individual.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CategoryRequest {

	private CategoryRequest() {
		throw new IllegalStateException("Utility class");
	}

	@AllArgsConstructor
	@Getter
	public static class PostCategoryDto {
		@NotBlank
		private String name;
		@NotNull
		private Long paletteId;
		@NotNull
		private boolean isShare;
	}
}
