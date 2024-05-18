package com.example.namo2.domain.individual.ui.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ScheduleRequest {
	private ScheduleRequest() {
		throw new IllegalStateException("Utility class");
	}

	@Getter
	@AllArgsConstructor
	public static class PostScheduleDto {
		@NotBlank
		private String name;
		@NotNull
		private Long startDate;
		@NotNull
		private Long endDate;
		@NotNull
		private Integer interval;
		private Set<Integer> alarmDate;
		private Double x;
		private Double y;
		private String locationName;
		private String kakaoLocationId;
		@NotNull
		private Long categoryId;
	}
}
