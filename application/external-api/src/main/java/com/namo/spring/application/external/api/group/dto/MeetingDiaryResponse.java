package com.namo.spring.application.external.api.group.dto;

import java.util.List;

import com.namo.spring.application.external.api.group.converter.GroupDiaryResponseConverter;
import com.namo.spring.core.common.utils.DateUtil;
import com.namo.spring.db.mysql.domains.group.domain.MoimMemo;

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
	public static class MeetingDiaryDto {
		private String name;
		private Long startDate;
		private String locationName;
		private List<MeetingUserDto> users;
		private List<MeetingActivityDto> meetingActivityDtos;

		public static MeetingDiaryDto fromMeetingMemo(MoimMemo meetingMemo,
			List<MeetingActivityDto> meetingActivityDtos) {
			List<MeetingDiaryResponse.MeetingUserDto> users = meetingMemo.getMoimSchedule()
				.getMoimScheduleAndUsers()
				.stream()
				.map(GroupDiaryResponseConverter::toMeetingUserDto)
				.toList();
			return MeetingDiaryResponse.MeetingDiaryDto.builder()
				.name(meetingMemo.getMoimSchedule().getName())
				.startDate(DateUtil.toSeconds(meetingMemo.getMoimSchedule().getPeriod().getStartDate()))
				.locationName(meetingMemo.getMoimSchedule().getLocation().getLocationName())
				.users(users)
				.meetingActivityDtos(meetingActivityDtos)
				.build();
		}
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class MeetingUserDto {
		private Long userId;
		private String userName;
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class MeetingActivityDto {
		private Long meetingActivityId;
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
	public static class DiaryDto {
		private Long scheduleId;
		private String name;
		private Long startDate;
		private String contents;
		private List<MeetingDiaryImageDto> images;
		private Long categoryId;
		private Long color;
		private String placeName;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class MeetingDiaryImageDto {
		private Long id;
		private String url;
	}
}
