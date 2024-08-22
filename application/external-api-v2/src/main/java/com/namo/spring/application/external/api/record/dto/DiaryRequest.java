package com.namo.spring.application.external.api.record.dto;

import java.util.List;

public class DiaryRequest {

	public static class CreateDiaryDto {
		private Long scheduleId;
		private String content;
		private List<String> imageUrls;
	}
}
