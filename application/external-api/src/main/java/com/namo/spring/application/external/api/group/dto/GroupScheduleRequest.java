package com.namo.spring.application.external.api.group.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GroupScheduleRequest {
	private GroupScheduleRequest() {
		throw new IllegalStateException("Util class");
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class PostGroupScheduleDto {
		@NotNull
		private Long groupId;
		@NotBlank
		private String name;

		@NotNull
		private Long startDate;
		@NotNull
		private Long endDate;
		@NotNull
		private Integer interval;

		@SuppressWarnings("checkstyle:MemberName")
		private Double x;
		@SuppressWarnings("checkstyle:MemberName")
		private Double y;
		private String locationName;
		private String kakaoLocationId;

		@NotNull
		private List<Long> users;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class PatchGroupScheduleDto {
		@NotNull
		private Long moimScheduleId;
		@NotBlank
		private String name;

		@NotNull
		private Long startDate;
		@NotNull
		private Long endDate;
		@NotNull
		private Integer interval;

		private Double x;
		private Double y;
		private String locationName;
		private String kakaoLocationId;

		@NotNull
		private List<Long> users;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class PatchGroupScheduleCategoryDto {
		@NotNull
		private Long moimScheduleId;

		@NotNull
		private Long categoryId;
	}

	@NoArgsConstructor
	@Getter
	public static class PostGroupScheduleAlarmDto {
		private Long moimScheduleId;
		private List<Integer> alarmDates;
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class PostGroupScheduleTextDto {
		private String text;
	}
}
