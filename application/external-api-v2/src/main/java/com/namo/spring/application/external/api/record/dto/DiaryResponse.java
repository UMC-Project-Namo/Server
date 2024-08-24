package com.namo.spring.application.external.api.record.dto;

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
}
