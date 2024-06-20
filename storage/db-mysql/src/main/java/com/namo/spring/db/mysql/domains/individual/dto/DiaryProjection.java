package com.namo.spring.db.mysql.domains.individual.dto;

import java.util.List;

import com.namo.spring.db.mysql.domains.individual.domain.Image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class DiaryProjection {
	@AllArgsConstructor
	@Getter
	@Builder
	public static class DiaryByUserDto {
		private Long scheduleId;
		private String contents;
		private List<Image> images;
	}
}
