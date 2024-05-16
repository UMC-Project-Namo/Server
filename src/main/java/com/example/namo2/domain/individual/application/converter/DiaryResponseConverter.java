package com.example.namo2.domain.individual.application.converter;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.example.namo2.domain.individual.domain.Image;
import com.example.namo2.domain.individual.domain.Schedule;
import com.example.namo2.domain.individual.ui.dto.DiaryResponse;
import com.example.namo2.domain.individual.ui.dto.ScheduleResponse;

public class DiaryResponseConverter {

	private DiaryResponseConverter() {
		throw new IllegalStateException("Utility class");
	}

	public static DiaryResponse.ScheduleIdDto toScheduleIdRes(Schedule schedule) {
		return DiaryResponse.ScheduleIdDto.builder()
			.scheduleId(schedule.getId())
			.build();
	}

	public static DiaryResponse.SliceDiaryDto toSliceDiaryDto(Slice<ScheduleResponse.DiaryDto> slice) {
		return DiaryResponse.SliceDiaryDto.builder()
			.content(slice.getContent())
			.currentPage(slice.getNumber())
			.size(slice.getSize())
			.first(slice.isFirst())
			.last(slice.isLast())
			.build();
	}

	public static DiaryResponse.GetDiaryByScheduleDto toGetDiaryByScheduleRes(
		Schedule schedule,
		List<String> imgUrls
	) {
		return DiaryResponse.GetDiaryByScheduleDto.builder()
			.contents(schedule.getContents())
			.urls(imgUrls)
			.build();
	}

	public static DiaryResponse.GetDiaryByUserDto toGetDiaryByUserRes(Schedule schedule) {
		return DiaryResponse.GetDiaryByUserDto.builder()
			.scheduleId(schedule.getId())
			.contents(schedule.getContents())
			.urls(schedule.getImages().stream()
				.map(Image::getImgUrl)
				.toList())
			.build();
	}
}
