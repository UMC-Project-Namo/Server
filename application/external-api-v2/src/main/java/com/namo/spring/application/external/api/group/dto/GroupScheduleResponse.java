package com.namo.spring.application.external.api.group.dto;

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
	public static class MonthlyGroupScheduleDto {
		private String name;
		private Long startDate;
		private Long endDate;
		private Integer interval;
		private List<MonthlyMeetingScheduleUserDto> users;
		private Long groupId;
		private Long meetingScheduleId;
		private boolean isCurMeetingSchedule = false;
		private Double x;
		private Double y;
		private String locationName;
		private String kakaoLocationId;
		private boolean hasDiaryPlace;
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class MonthlyMeetingScheduleUserDto {
		private Long userId;
		private String userName;
		private Integer color;
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class AllGroupScheduleDto {
		private String name;
		private Long startDate;
		private Long endDate;
		private Integer interval;
		private List<MeetingScheduleUserDto> users;
		private Long groupId;
		private Long meetingScheduleId;
		private boolean isCurMeetingSchedule = false;
		private Double x;
		private Double y;
		private String locationName;
		private String kakaoLocationId;
		private boolean hasDiaryPlace;
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class MeetingScheduleUserDto {
		private Long userId;
		private String userName;
		private Integer color;
	}

}
