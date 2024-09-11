package com.namo.spring.application.external.api.record.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ActivityResponse {

	@Builder
	@Getter
	@AllArgsConstructor
	@Schema(description = "활동 조회 DTO")
	public static class ActivityInfoDto {
		private String activityTitle;
		private List<ActivityParticipantDto> activityParticipants;
		private LocalDateTime activityStartDate;
		private LocalDateTime activityEndDate;
		private ActivityLocationDto activityLocation;
		private BigDecimal totalAmount;
		private String tag;
	}

	@Builder
	@Getter
	@AllArgsConstructor
	@Schema(description = "활동 참여자 DTO")
	public static class ActivityParticipantDto {
		private Long participantMemberId;
		private String participantName;
	}

	@Builder
	@Getter
	@AllArgsConstructor
	@Schema(description = "활동 장소 DTO")
	public static class ActivityLocationDto {
		private String kakaoLocationId;
		private String locationName;
	}
}
