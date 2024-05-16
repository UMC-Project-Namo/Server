package com.example.namo2.domain.group.ui.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GroupScheduleResponse {
	private GroupScheduleResponse() {
		throw new IllegalStateException("Util Class");
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
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

		@QueryProjection
		@Builder
		public MoimScheduleDto(
			String name,
			LocalDateTime startDate,
			LocalDateTime endDate,
			Integer interval,
			Long groupId,
			Long moimScheduleId,
			Double x,
			Double y,
			String locationName,
			String kakaoLocationId
		) {
			this.name = name;
			this.startDate = startDate.atZone(ZoneId.systemDefault())
				.toInstant()
				.getEpochSecond();
			this.endDate = endDate.atZone(ZoneId.systemDefault())
				.toInstant()
				.getEpochSecond();
			this.interval = interval;
			this.groupId = groupId;
			this.moimScheduleId = moimScheduleId;
			this.x = x;
			this.y = y;
			this.locationName = locationName;
			this.kakaoLocationId = kakaoLocationId;
		}

		@QueryProjection
		@Builder
		public MoimScheduleDto(
			String name,
			LocalDateTime startDate,
			LocalDateTime endDate,
			Integer interval,
			Long userId,
			String userName,
			Integer color
		) {
			this.name = name;
			this.startDate = startDate.atZone(ZoneId.systemDefault())
				.toInstant()
				.getEpochSecond();
			this.endDate = endDate.atZone(ZoneId.systemDefault())
				.toInstant()
				.getEpochSecond();
			this.interval = interval;
			this.users.add(new MoimScheduleUserDto(userId, userName, color));
			this.hasDiaryPlace = false;
		}

		public void setUsers(
			List<MoimScheduleUserDto> users,
			boolean isCurMoimSchedule,
			boolean hasDiaryPlace
		) {
			this.users = users;
			this.isCurMoimSchedule = isCurMoimSchedule;
			this.hasDiaryPlace = hasDiaryPlace;
		}
	}

	@NoArgsConstructor
	@Getter
	public static class MoimScheduleUserDto {
		private Long userId;
		private String userName;
		private Integer color;

		@Builder
		public MoimScheduleUserDto(Long userId, String userName, Integer color) {
			this.userId = userId;
			this.userName = userName;
			this.color = color;
		}
	}

}
