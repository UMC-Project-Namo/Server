package com.namo.spring.application.external.api.group.dto;

import java.util.Arrays;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MeetingDiaryRequest {

	private MeetingDiaryRequest() {
		throw new IllegalStateException("Utill Classes");
	}

	@Getter
	@NoArgsConstructor
	public static class GroupDiaryLocationDtos {
		List<LocationDto> locationDtos;
	}

	@Getter
	@NoArgsConstructor
	public static class LocationDto {
		private String name;
		private Integer money;
		private List<Long> participants;

		public LocationDto(String name, String money, String participants) {
			this.name = name;
			this.money = Integer.valueOf(money);
			this.participants = Arrays.stream(participants.replace(" ", "").split(","))
				.map(Long::valueOf)
				.toList();
		}
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class PostMeetingMemoDto {
		@Schema(description = "모임 메모")
		private String text;
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class PatchMeetingMemoDto {
		@Schema(description = "모임 메모")
		private String text;
	}
}
