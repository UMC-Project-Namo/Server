package com.namo.spring.application.external.api.individual.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ScheduleResponse {
	private ScheduleResponse() {
		throw new IllegalStateException("Utility class");
	}

	@Getter
	@NoArgsConstructor
	@Builder
	@AllArgsConstructor
	public static class IndividualMonthlyScheduleDto {
		private Long scheduleId;
		private String name;
		private Long startDate;
		private Long endDate;
		private List<Integer> alarmDate;
		private Integer interval;
		private Double x;
		private Double y;
		private String locationName;
		private String kakaoLocationId;
		private Long categoryId;
		private Boolean hasDiary;
		private boolean isMeetingSchedule;
	}

	@Getter
	@NoArgsConstructor
	@Builder
	@AllArgsConstructor
	public static class MeetingMonthlyScheduleDto {
		private Long scheduleId;
		private String name;
		private Long startDate;
		private Long endDate;
		private List<Integer> alarmDate;
		private Integer interval;
		private Double x;
		private Double y;
		private String locationName;
		private String kakaoLocationId;
		private Long categoryId;
		private Boolean hasDiary;
		private boolean isMeetingSchedule;
	}

	@Getter
	@NoArgsConstructor
	@Builder
	@AllArgsConstructor
	public static class AllMonthlyScheduleDto {
		private Long scheduleId;
		private String name;
		private Long startDate;
		private Long endDate;
		private List<Integer> alarmDate;
		private Integer interval;
		private Double x;
		private Double y;
		private String locationName;
		private String kakaoLocationId;
		private Long categoryId;
		private Boolean hasDiary;
		private boolean isMeetingSchedule;
	}

	@Getter
	@NoArgsConstructor
	@Builder
	@AllArgsConstructor
	public static class AllMonthlyMeetingScheduleDto {
		private Long scheduleId;
		private String name;
		private Long startDate;
		private Long endDate;
		private List<Integer> alarmDate;
		private Integer interval;
		private Double x;
		private Double y;
		private String locationName;
		private String kakaoLocationId;
		private Long categoryId;
		private Boolean hasDiary;
		private boolean isMeetingSchedule;
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class GetDiaryByUserDto {
		private Long scheduleId;
		private String contents;
		private List<String> urls;
	}

	@AllArgsConstructor
	@Getter
	@Builder
	public static class GetDiaryByScheduleDto {
		private String contents;
		private List<String> urls;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class SliceDiaryDto {
		private List<DiaryDto> content;
		private int currentPage;
		private int size;
		private boolean first;
		private boolean last;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class DiaryDto {
		private Long scheduleId;
		private String name;
		private Long startDate;
		private String contents;
		private List<String> urls;
		private Long categoryId;
		private Long color;
		private String placeName;
	}
}
