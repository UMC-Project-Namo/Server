package com.namo.spring.application.external.api.group.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class MeetingActivityResponse {

	private MeetingActivityResponse() {
		throw new IllegalStateException("Utill Classes");
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class MeetingActivitiesDto {
		private String name;
		private Long startDate;
		private String locationName;
		private List<GroupUserDto> users;
		private List<MeetingActivityDto> meetingActivities;
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
	public static class MeetingActivityDto {
		private Long id;
		private String name;
		private Integer money;
		private List<Long> participants;
		private List<MeetingActivityImageDto> images;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class MeetingActivityImageDto {
		private Long id;
		private Long url;
	}
}
