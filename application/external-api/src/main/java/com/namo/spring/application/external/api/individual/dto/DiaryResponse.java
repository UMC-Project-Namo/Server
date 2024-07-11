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
	@AllArgsConstructor
	@Builder
	public static class ScheduleIdDto {
		private Long scheduleId;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class SliceDiaryDto {
		private List<ScheduleResponse.DiaryDto> content;
		private int currentPage;
		private int size;
		private boolean first;
		private boolean last;
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class GetDiaryByUserDto {
		private Long scheduleId;
		private String contents;
		private List<String> urls;
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class GetDiaryByScheduleDto {
		private String contents;
		private List<DiaryImageDto> images;
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class DiaryImageDto {
		private String id;
		private String url;
	}

}
