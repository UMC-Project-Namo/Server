package com.namo.spring.application.external.api.group.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class GroupScheduleResponse {
	private GroupScheduleResponse() {
		throw new IllegalStateException("Util Class");
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class MoimScheduleDto {
		private String name;
		private Long startDate;
		private Long endDate;
		private Integer interval;
		private List<MoimScheduleUserDto> users = new ArrayList<>();
		private Long groupId;
		private Long moimScheduleId;
		private boolean isCurMoimSchedule = false;
		private Double x;
		private Double y;
		private String locationName;
		private String kakaoLocationId;
		private boolean hasDiaryPlace;
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class MoimScheduleUserDto {
		private Long userId;
		private String userName;
		private Integer color;
	}

}
