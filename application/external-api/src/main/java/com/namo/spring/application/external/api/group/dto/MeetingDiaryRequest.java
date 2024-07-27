package com.namo.spring.application.external.api.group.dto;

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
	public static class MeetingDiaryLocationDtos {
		List<LocationDto> locationDtos;
	}

	@Getter
	@NoArgsConstructor
	public static class LocationDto {
		private String name;
		private Integer money;
		private List<Long> participants;

		public LocationDto(String name, String money, List<Long> participants) {
			this.name = name;
			this.money = Integer.valueOf(money);
			this.participants = participants;
		}
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Schema(title = "개인 페이지 모임 메모 추가")
	public static class PostMeetingScheduleTextDto {
		@Schema(description = "개인 페이지 모임 메모", example = "개인 페이지 모임 메모 예시")
		private String text;
	}
}
