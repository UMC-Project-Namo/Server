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
		public CategoryInfoDto categoryInfo;
		private LocalDateTime scheduleDate;
		private Long scheduleId;
		private String title;
		private DiarySummaryDto diarySummary;
		private int scheduleType;
		private ParticipantInfo participantInfo;
	}

	@Builder
	@Getter
	@AllArgsConstructor
	public static class ParticipantInfo {
		private int participantsCount;
		private String participantsNames;
	}

	@Builder
	@Getter
	@AllArgsConstructor
	public static class DiarySummaryDto {
		private Long diaryId;
		private String content;
		private List<DiaryImageDto> diaryImages;
	}

	@Builder
	@Getter
	@AllArgsConstructor
	public static class DiaryExistDateDto {
		private int year;
		private int month;
		private List<Integer> dates;
	}

}
