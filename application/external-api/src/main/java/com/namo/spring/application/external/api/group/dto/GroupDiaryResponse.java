package com.namo.spring.application.external.api.group.dto;

import java.util.List;

import com.namo.spring.application.external.api.group.converter.GroupDiaryResponseConverter;
import com.namo.spring.core.common.utils.DateUtil;
import com.namo.spring.db.mysql.domains.group.domain.MoimMemo;

import io.swagger.v3.oas.annotations.media.Schema;

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
	@Schema(title = "모임 기록 조회")
	public static class GroupDiaryDto {
		@Schema(description = "모임 기록 제목")
		private String name;
		@Schema(description = "모임 기록 시작 날짜 (Unix timestamp)")
		private Long startDate;
		@Schema(description = "모임 기록 장소")
		private String locationName;
		@Schema(description = "모임 참여자 list")
		private List<GroupUserDto> users;
		@Schema(description = "모임 활동 list")
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
		@Schema(description = "사용자 id")
		private Long userId;
		@Schema(description = "사용자 이름")
		private String userName;
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class MoimActivityDto {
		@Schema(description = "모임 활동 id")
		private Long moimActivityId;
		@Schema(description = "모임 활동 제목")
		private String name;
		@Schema(description = "모임 활동 회비")
		private Integer money;
		@Schema(description = "모임 활동 참여자 id")
		private List<Long> participants;
		@Schema(description = "모임 활동 이미지 list")
		private List<MoimActivityImageDto> images;
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class MoimActivityImageDto {
		@Schema(description = "이미지 id")
		private Long id;
		@Schema(description = "이미지 url")
		private String url;
	}
}
