package com.namo.spring.application.external.domain.test.ui.dto;

import lombok.Builder;
import lombok.Getter;

public class TestResponse {

	private TestResponse() {
		throw new IllegalStateException("Utility class");
	}

	@Getter
	@Builder
	public static class LogTestDto {
		private String text;
		private Integer number;
	}

	@Getter
	@Builder
	public static class TestDto {
		private String test;
	}
}
