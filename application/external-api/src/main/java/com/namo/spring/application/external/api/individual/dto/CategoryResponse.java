package com.namo.spring.application.external.api.individual.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CategoryResponse {

	private CategoryResponse() {
		throw new IllegalStateException("Utility class");
	}

	@Getter
	@AllArgsConstructor
	public static class CategoryIdDto {
		private Long id;
	}

	@Getter
	@AllArgsConstructor
	public static class CategoryDto {
		private Long categoryId;
		private String name;
		private Long paletteId;
		private Boolean isShare;
	}
}
