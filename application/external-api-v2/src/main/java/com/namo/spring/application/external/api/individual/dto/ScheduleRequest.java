package com.namo.spring.application.external.api.individual.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ScheduleRequest {
	private ScheduleRequest() {
		throw new IllegalStateException("Utility class");
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class createScheduleDto {
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

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class updateScheduleDto {
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
