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

	@AllArgsConstructor
	@Getter
	@Builder
	public static class GroupDiaryDto {
		private String name;
		private Long startDate;
		private String locationName;
		private List<GroupUserDto> users;
		private List<MoimActivityDto> moimActivityDtos;
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class GroupUserDto {
		private Long userId;
		private String userName;
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class MoimActivityDto {
		private Long moimActivityId;
		private String name;
		private Integer money;
		private List<Long> participants;
		private List<String> urls;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class SliceDiaryDto<T> {
		private List<T> content;
		private int currentPage;
		private int size;
		private boolean first;
		private boolean last;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@Schema(title = "개인 페이지 모임 기록 상세 조회")
	public static class DiaryDetailDto {
		@Schema(description = "일정 id")
		private Long scheduleId;
		@Schema(description = "모임 일정 title")
		private String name;
		@Schema(description = "모임 일정 시작 날짜 (유닉스 시간)")
		private Long startDate;
		@Schema(description = "모임 일정에 해당하는 기록 메모")
		private String contents;
		@Schema(description = "모임 활동 이미지 (3개)")
		private List<String> urls;
		@Schema(description = "카테고리 id")
		private Long categoryId;
		@Schema(description = "카테고리 color")
		private Long color;
		@Schema(description = "장소 이름")
		private String placeName;
	}
}
