package com.namo.spring.application.external.api.group.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class MeetingDiaryResponse {
	private MeetingDiaryResponse() {
		throw new IllegalStateException("Utill Classes");
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class MonthlyMeetingActivityDto {
		private List<MonthlyMeetingActivityInfoDto> content;
		private int currentPage;
		private int size;
		private boolean first;
		private boolean last;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class MonthlyMeetingActivityInfoDto {
		private Long scheduleId;
		private String name;
		private Long startDate;
		private String contents;
		private List<MonthlyDiaryInfoImageDto> images;
		private Long categoryId;
		private Long color;
		private String placeName;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class MonthlyDiaryInfoImageDto {
		private Long id;
		private Long url;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class MeetingDiaryInfoDto {
		private Long scheduleId;
		private String name;
		private Long startDate;
		private String contents;
		private List<MeetingDiaryInfoImageDto> images;
		private Long categoryId;
		private Long color;
		private String placeName;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class MeetingDiaryInfoImageDto {
		private Long id;
		private Long url;
	}
}
