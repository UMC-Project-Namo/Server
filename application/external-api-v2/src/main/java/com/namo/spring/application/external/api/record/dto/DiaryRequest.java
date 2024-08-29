package com.namo.spring.application.external.api.record.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;

public class DiaryRequest {

	@Getter
	public static class CreateDiaryDto {
		@Schema(description = "스케줄 ID를 넣어주세요", example = "1")
		private Long scheduleId;
		@Schema(description = "일기 내용을 넣어주세요", example = "오늘은 즐거운 하루였어요")
		private String content;
		@Schema(description = "즐거움 지수를 넣어주세요 1~3", example = "3.0")
		private double enjoyRating;
		private List<CreateDiaryImageDto> diaryImages;
	}

	@Getter
	public static class UpdateDiaryDto {
		@Schema(description = "일기 내용을 넣어주세요", example = "오늘은 즐거운 하루였어요")
		private String content;
		@Schema(description = "즐거움 지수를 넣어주세요 1~3", example = "3.0")
		private double enjoyRating;
		private List<CreateDiaryImageDto> diaryImages;
	}

	@Getter
	public static class CreateDiaryImageDto {
		@Schema(description = "이미지 순서를 넣어주세요 1번부터 ~", example = "1")
		private Integer orderNumber;
		@Schema(description = "이미지 URL을 넣어주세요", example = "https://static.namong.shop/origin/diary/image.jpg")
		private String imageUrl;
	}
}
