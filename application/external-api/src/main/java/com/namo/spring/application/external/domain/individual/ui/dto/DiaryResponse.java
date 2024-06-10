package com.example.namo2.domain.individual.ui.dto;

import java.util.List;

import org.springframework.data.domain.Slice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class DiaryResponse {

	private DiaryResponse() {
		throw new IllegalStateException("Utility class");
	}

	@Getter
	@AllArgsConstructor
	@Builder
	public static class ScheduleIdDto {
		private Long scheduleId;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class SliceDiaryDto {
		private List<ScheduleResponse.DiaryDto> content;
		private int currentPage;
		private int size;
		private boolean first;
		private boolean last;

		public SliceDiaryDto(Slice<ScheduleResponse.DiaryDto> slice) {
			this.content = slice.getContent();
			this.currentPage = slice.getNumber();
			this.size = content.size();
			this.first = slice.isFirst();
			this.last = slice.isLast();
		}
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
}
