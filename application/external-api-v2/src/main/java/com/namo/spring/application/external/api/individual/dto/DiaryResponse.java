package com.namo.spring.application.external.api.individual.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class DiaryResponse {

	private DiaryResponse() {
		throw new IllegalStateException("Utility class");
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class MonthlyDiaryDto {
		private List<MonthlyDiaryContent> content;
		private int currentPage;
		private int size;
		private boolean first;
		private boolean last;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class MonthlyDiaryContent {
		private Long scheduleId;
		private String name;
		private Long startDate;
		private String contents;
		private List<String> urls;
		private Long categoryId;
		private Long color;
		private String placeName;
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class AllDiaryDto {
		private Long scheduleId;
		private String contents;
		private List<AllDiaryImageDto> images;
	}

	public static class AllDiaryImageDto {
		private Long id;
		private String url;
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class DiaryInfoDto {
		private String contents;
		private List<DiaryInfoImageDto> images;
	}

	public static class DiaryInfoImageDto {
		private Long id;
		private String url;
	}

}
