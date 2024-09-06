package com.namo.spring.application.external.api.schedule.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ScheduleResponse {

	private ScheduleResponse() {
		throw new IllegalStateException("Util Class");
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class ScheduleSummaryDto {
		private Long scheduleId;
		private String scheduleTitle;
		private LocalDateTime scheduleStartDate;
		private LocationInfoDto location;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class LocationInfoDto {
		private Long locationId;
		private String locationName;
	}

}
