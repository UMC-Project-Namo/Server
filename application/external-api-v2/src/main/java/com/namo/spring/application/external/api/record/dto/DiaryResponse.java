package com.namo.spring.application.external.api.record.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class DiaryResponse {

	@Builder
	@Getter
	@AllArgsConstructor
	@Schema(description = "일기 상세 정보 DTO")
	public static class DiaryDto {
		@Schema(description = "일기 ID", example = "2")
		private Long diaryId;
		@Schema(description = "일기 내용", example = "재미있는 하루였다.. ㅎ")
		private String content;
		@Schema(description = "재미도", example = "1.0")
		private double enjoyRating;
		private List<DiaryImageDto> diaryImages;
	}

	@Builder
	@Getter
	@AllArgsConstructor
	@Schema(description = "일기 이미지 정보 DTO")
	public static class DiaryImageDto {
		@Schema(description = "정렬 순서", example = "1")
		private Integer orderNumber;
		@Schema(description = "이미지 ID", example = "34")
		private Long diaryImageId;
		@Schema(description = "이미지 URL", example = "https://static.namong.shop/resized/origin/diary/image.png")
		private String imageUrl;
	}

	@Builder
	@Getter
	@AllArgsConstructor
	@Schema(description = "일기 보관함 DTO")
	public static class DiaryArchiveDto {
		private CategoryInfoDto categoryInfo;
		private LocalDateTime scheduleStartDate;
		private LocalDateTime scheduleEndDate;
		@Schema(description = "스케줄 ID", example = "11")
		private Long scheduleId;
		@Schema(description = "스케줄 제목", example = "저녁 약속")
		private String title;
		private DiarySummaryDto diarySummary;
		@Schema(description = "개인 스케줄 : 0, 모임 스케줄 : 1", example = "0")
		private int scheduleType;
		private ParticipantInfo participantInfo;
	}

	@Builder
	@Getter
	@AllArgsConstructor
	@Schema(description = "스케줄 참여자 정보 DTO")
	public static class ParticipantInfo {
		@Schema(description = "참여자 수", example = "1")
		private int participantsCount;
		@Schema(description = "참여자 이름 목록", example = "홍길동, 나몽")
		private String participantsNames;
	}

	@Builder
	@Getter
	@AllArgsConstructor
	@Schema(description = "일기 정보 DTO")
	public static class DiarySummaryDto {
		@Schema(description = "일기 ID", example = "2")
		private Long diaryId;
		@Schema(description = "일기 내용", example = "오늘 너무 재미있었다. ㅎㅎ")
		private String content;
		private List<DiaryImageDto> diaryImages;
	}

	@Builder
	@Getter
	@AllArgsConstructor
	@Schema(description = "일기 존재 날짜 DTO")
	public static class DiaryExistDateDto {
		private int year;
		private int month;
		@Schema(description = "일기가 존재하는 날짜", example = "1, 2, 3")
		private List<Integer> dates;
	}

	@Builder
	@Getter
	@AllArgsConstructor
	@Schema(description = "일별 일기에 대한 스케줄 DTO")
	public static class DayOfDiaryDto {
		@Schema(description = "개인 스케줄 : 0, 모임 스케줄 : 1", example = "0")
		private int scheduleType;
		private CategoryInfoDto categoryInfo;
		private LocalDateTime scheduleStartDate;
		private LocalDateTime scheduleEndDate;
		@Schema(description = "스케줄 이름", example = "점심 약속")
		private String scheduleTitle;
		@Schema(description = "일기 ID", example = "2")
		private Long diaryId;
		private ParticipantInfo participantInfo;
	}

	@Builder
	@Getter
	@AllArgsConstructor
	@Schema(description = "카테고리 정보 DTO")
	public static class CategoryInfoDto {
		@Schema(description = "카테고리 이름", example = "개인 일정")
		private String name;
		@Schema(description = "카테고리 색상", example = "1")
		private int color;
	}
}
