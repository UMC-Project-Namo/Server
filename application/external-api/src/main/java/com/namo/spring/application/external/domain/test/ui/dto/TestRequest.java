package com.namo.spring.application.external.domain.test.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TestRequest {

	private TestRequest() {
		throw new IllegalStateException("Utility class");
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class LogTestDto {
		private String text;
		private Integer number;
	}
}
