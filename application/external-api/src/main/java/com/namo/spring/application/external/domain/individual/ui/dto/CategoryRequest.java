package com.example.namo2.domain.individual.ui.dto;

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
