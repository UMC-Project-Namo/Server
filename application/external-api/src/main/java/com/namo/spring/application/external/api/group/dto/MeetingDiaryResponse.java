package com.namo.spring.application.external.api.group.dto;

import java.util.List;

import com.namo.spring.application.external.api.group.converter.GroupDiaryResponseConverter;
import com.namo.spring.core.common.utils.DateUtil;
import com.namo.spring.db.mysql.domains.group.domain.MoimMemo;

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
	@Schema(title = "모임 기록 조회")
	public static class MeetingDiaryDto {
		@Schema(description = "모임 기록 제목")
		private String name;
		@Schema(description = "모임 기록 시작 날짜 (Unix timestamp)")
		private Long startDate;
		@Schema(description = "모임 기록 장소")
		private String locationName;
		@Schema(description = "모임 참여자 list")
		private List<MeetingUserDto> users;
		@Schema(description = "모임 활동 list")
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
		@Schema(description = "사용자 id")
		private Long userId;
		@Schema(description = "사용자 이름")
		private String userName;
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class MeetingActivityDto {
		@Schema(description = "모임 활동 id")
		private Long meetingActivityId;
		@Schema(description = "모임 활동 제목")
		private String name;
		@Schema(description = "모임 활동 회비")
		private Integer money;
		@Schema(description = "모임 활동 참여자 id")
		private List<Long> participants;
		@Schema(description = "모임 활동 이미지 list")
		private List<MeetingActivityImageDto> images;
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class MeetingActivityImageDto {
		@Schema(description = "이미지 id")
		private Long id;
		@Schema(description = "이미지 url")
		private String url;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@Schema(title = "개인 페이지 모임 기록 월별 조회")
	public static class SliceDiaryDto<T> {
		@Schema(description = "개인 페이지 모임 기록 상세")
		private List<T> content;
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
	public static class DiaryDto {
		@Schema(description = "일정 id")
		private Long scheduleId;
		@Schema(description = "모임 일정 title")
		private String name;
		@Schema(description = "모임 일정 시작 날짜 (Unix timestamp)")
		private Long startDate;
		@Schema(description = "모임 일정에 해당하는 메모")
		private String contents;
		@Schema(description = "모임 활동 이미지 (3개)")
		private List<MeetingDiaryImageDto> images;
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
	public static class MeetingDiaryImageDto {
		@Schema(description = "이미지 id")
		private Long id;
		@Schema(description = "이미지 url")
		private String url;
	}
}
