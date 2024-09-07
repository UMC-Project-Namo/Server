package com.namo.spring.application.external.api.schedule.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ScheduleResponse {

	private ScheduleResponse() {
		throw new IllegalStateException("Util Class");
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class ScheduleSummaryDto {
		@Schema(description = "스케줄 ID 입니다.", example = "2")
		private Long scheduleId;
		@Schema(description = "스케줄 제목입니다.", example = "코딩스터디")
		private String scheduleTitle;
		@Schema(description = "스케줄 시작날짜입니다.")
		private LocalDateTime scheduleStartDate;
		private LocationInfoDto locationInfo;
		private CategoryInfoDto categoryInfo;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class LocationInfoDto {
		@Schema(description = "카카오 장소 id입니다.", example = "26338954")
		private String kakaoLocationId;
		@Schema(description = "장소 이름입니다.", example = "탐앤탐스 탐스커버리 건대점")
		private String locationName;
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
