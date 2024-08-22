package com.namo.spring.application.external.api.record.dto;

import java.util.List;

import lombok.Getter;

public class DiaryRequest {

	@Getter
	public static class CreateDiaryDto {
		private Long scheduleId;
		private String content;
		private double enjoyRating;
		private List<CreateDiaryImageDto> diaryImages;
	}

	@Getter
	public static class CreateDiaryImageDto {
		private Integer orderNumber;
		private String imageUrl;
	}
}
