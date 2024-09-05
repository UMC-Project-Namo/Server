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
		private CategoryInfoDto categoryInfo;
		private LocalDateTime scheduleStartDate;
		private LocalDateTime scheduleEndDate;
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

	@Builder
	@Getter
	@AllArgsConstructor
	public static class DayOfDiaryDto {
		private int scheduleType;
		private CategoryInfoDto categoryInfo;
		private LocalDateTime scheduleStartDate;
		private LocalDateTime scheduleEndDate;
		private String scheduleTitle;
		private Long diaryId;
		private ParticipantInfo participantInfo;
	}

	@Builder
	@Getter
	@AllArgsConstructor
	public static class CategoryInfoDto {
		private String name;
		private int color;
	}
}
