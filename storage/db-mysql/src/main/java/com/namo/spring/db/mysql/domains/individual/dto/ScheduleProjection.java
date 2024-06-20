package com.namo.spring.db.mysql.domains.individual.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.namo.spring.db.mysql.domains.individual.domain.Alarm;
import com.namo.spring.db.mysql.domains.individual.domain.Image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ScheduleProjection {
	@Getter
	@NoArgsConstructor
	@Builder
	@AllArgsConstructor
	public static class ScheduleDto {
		private Long scheduleId;
		private String name;
		private LocalDateTime startDate;
		private LocalDateTime endDate;
		private List<Alarm> alarms;
		private Integer interval;
		private Double x;
		private Double y;
		private String locationName;
		private String kakaoLocationId;
		private Long categoryId;
		private Boolean hasDiary;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class DiaryDto {
		private Long scheduleId;
		private String name;
		private LocalDateTime startDate;
		private String contents;
		private List<Image> images;
		private Long categoryId;
		private Long color;
		private String placeName;
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class DiaryByUserDto {
		private Long scheduleId;
		private String contents;
		private List<Image> images;
	}
}
