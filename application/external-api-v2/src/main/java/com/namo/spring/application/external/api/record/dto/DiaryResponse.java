package com.namo.spring.application.external.api.record.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class DiaryResponse {

	@Builder
	@Getter
	@AllArgsConstructor
	public static class DiaryDto {
		private Long diaryId;
		private String content;
		private double enjoyRating;
		private List<DiaryImageDto> diaryImages;
	}

	@Builder
	@Getter
	@AllArgsConstructor
	public static class DiaryImageDto {
		private Integer orderNumber;
		private Long diaryImageId;
		private String imageUrl;
	}

	@Builder
	@Getter
	@AllArgsConstructor
	public static class DiaryArchiveDto {
		private LocalDateTime scheduleDate;
		private Long scheduleId;
		private DiarySummaryDto diarySummary;
		private boolean isMeetingSchedule;
		private int participantsCount;
		private String participantsNames;
	}

	@Builder
	@Getter
	@AllArgsConstructor
	public static class DiarySummaryDto {
		private Long diaryId;
		private String title;
		private String content;
		private DiaryImageDto diaryImage;
	}

}