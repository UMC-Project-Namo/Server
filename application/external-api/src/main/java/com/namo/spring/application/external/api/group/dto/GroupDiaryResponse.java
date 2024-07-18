package com.namo.spring.application.external.api.group.dto;

import java.util.List;

import com.namo.spring.application.external.api.group.converter.GroupDiaryResponseConverter;
import com.namo.spring.core.common.utils.DateUtil;
import com.namo.spring.db.mysql.domains.group.domain.MoimMemo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * v1
 */
public class GroupDiaryResponse {
	private GroupDiaryResponse() {
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

		public static GroupDiaryDto fromMoimMemo(MoimMemo moimMemo, List<MoimActivityDto> moimActivityDtos) {
			List<GroupDiaryResponse.GroupUserDto> users = moimMemo.getMoimSchedule().getMoimScheduleAndUsers().stream()
				.map(GroupDiaryResponseConverter::toGroupUserDto)
				.toList();
			return GroupDiaryResponse.GroupDiaryDto.builder()
				.name(moimMemo.getMoimSchedule().getName())
				.startDate(DateUtil.toSeconds(moimMemo.getMoimSchedule().getPeriod().getStartDate()))
				.locationName(moimMemo.getMoimSchedule().getLocation().getLocationName())
				.users(users)
				.moimActivityDtos(moimActivityDtos)
				.build();
		}
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
		private List<MoimActivityImageDto> moimActivityImages;
	}

	public static class MoimActivityImageDto {
		private Long id;
		private String url;
	}
}
