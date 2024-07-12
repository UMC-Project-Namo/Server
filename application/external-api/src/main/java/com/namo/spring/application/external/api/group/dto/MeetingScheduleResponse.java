package com.namo.spring.application.external.api.group.dto;

import java.util.ArrayList;
import java.util.List;

import com.namo.spring.core.common.utils.DateUtil;
import com.namo.spring.db.mysql.domains.group.domain.MoimSchedule;
import com.namo.spring.db.mysql.domains.individual.domain.Schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class MeetingScheduleResponse {
	private MeetingScheduleResponse() {
		throw new IllegalStateException("Util Class");
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class MeetingScheduleDto {
		private String name;
		private Long startDate;
		private Long endDate;
		private Integer interval;
		private List<MeetingScheduleUserDto> users = new ArrayList<>();
		private Long groupId;
		private Long meetingScheduleId;
		private boolean isCurGroupSchedule = false;
		private Double x;
		private Double y;
		private String locationName;
		private String kakaoLocationId;
		private boolean hasDiaryPlace;

		public static MeetingScheduleDto fromSchedule(Schedule schedule,
			List<MeetingScheduleUserDto> meetingScheduleUserDtos) {
			return MeetingScheduleResponse.MeetingScheduleDto.builder()
				.name(schedule.getName())
				.startDate(DateUtil.toSeconds(schedule.getPeriod().getStartDate()))
				.endDate(DateUtil.toSeconds(schedule.getPeriod().getEndDate()))
				.interval(schedule.getPeriod().getDayInterval())
				.users(meetingScheduleUserDtos)
				.hasDiaryPlace(false)
				.build();
		}

		public static MeetingScheduleDto fromMeetingSchedule(MoimSchedule meetingSchedule,
			List<MeetingScheduleUserDto> meetingScheduleUserDtos, boolean isCurGroupSchedule) {
			return MeetingScheduleDto.builder()
				.name(meetingSchedule.getName())
				.startDate(DateUtil.toSeconds(meetingSchedule.getPeriod().getStartDate()))
				.endDate(DateUtil.toSeconds(meetingSchedule.getPeriod().getEndDate()))
				.interval(meetingSchedule.getPeriod().getDayInterval())
				.groupId(meetingSchedule.getMoim().getId())
				.meetingScheduleId(meetingSchedule.getId())
				.x(meetingSchedule.getLocation().getX())
				.y(meetingSchedule.getLocation().getY())
				.locationName(meetingSchedule.getLocation().getLocationName())
				.kakaoLocationId(meetingSchedule.getLocation().getKakaoLocationId())
				.users(meetingScheduleUserDtos)
				.isCurGroupSchedule(isCurGroupSchedule)
				.hasDiaryPlace(meetingSchedule.getMoimMemo() != null)
				.build();
		}
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
