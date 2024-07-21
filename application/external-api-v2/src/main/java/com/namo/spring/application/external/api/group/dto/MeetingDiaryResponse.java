package com.namo.spring.application.external.api.group.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

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
	public static class SliceDiaryDto {
		@Schema(description = "개인 페이지 모임 기록 상세")

		private List<MonthlyMeetingActivityInfoDto> content;
		@Schema(description = "현재 페이지 번호 (0~)")
		private int currentPage;
		@Schema(description = "한 페이지에 표시될 항목 수")
		private int size;
		@Schema(description = "현재 페이지가 첫 페이지인지")
		private boolean first;
		@Schema(description = "현재 페이지가 마지막 페이지인지")
		private boolean last;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@Schema(title = "개인 페이지 모임 기록 상세 조회")
	public static class MonthlyMeetingActivityInfoDto {
		@Schema(description = "일정 id")
		private Long scheduleId;
		@Schema(description = "모임 일정 title")
		private String name;
		@Schema(description = "모임 일정 시작 날짜 (유닉스 시간)")
		private Long startDate;
		@Schema(description = "모임 일정에 해당하는 기록 메모")
		private String contents;
		@Schema(description = "모임 활동 이미지 (3개)")
		private List<MonthlyDiaryInfoImageDto> images;
		@Schema(description = "카테고리 id")
		private Long categoryId;
		@Schema(description = "카테고리 color")
		private Long color;
		@Schema(description = "장소 이름")
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
