package com.namo.spring.application.external.api.individual.converter;

import java.util.stream.Collectors;

import org.springframework.data.domain.Slice;

import com.namo.spring.application.external.api.individual.dto.DiaryResponse;
import com.namo.spring.db.mysql.domains.individual.domain.Image;
import com.namo.spring.db.mysql.domains.individual.domain.Schedule;
import com.namo.spring.db.mysql.domains.individual.dto.ScheduleProjection;

public class DiaryResponseConverter {

	private DiaryResponseConverter() {
		throw new IllegalStateException("Utility class");
	}

	public static DiaryResponse.ScheduleIdDto toScheduleIdRes(Schedule schedule) {
		return DiaryResponse.ScheduleIdDto.builder()
			.scheduleId(schedule.getId())
			.build();
	}

	public static DiaryResponse.SliceDiaryDto toSliceDiaryDto(Slice<ScheduleProjection.DiaryDto> slice) {
		return DiaryResponse.SliceDiaryDto.builder()
			.content(slice.stream().map(ScheduleResponseConverter::toDiaryDto).collect(Collectors.toList()))
			.currentPage(slice.getNumber())
			.size(slice.getSize())
			.first(slice.isFirst())
			.last(slice.isLast())
			.build();
	}

	public static DiaryResponse.GetDiaryByScheduleDto toGetDiaryByScheduleRes(
		Schedule schedule
	) {
		return DiaryResponse.GetDiaryByScheduleDto.builder()
			.contents(schedule.getContents())
			.images(schedule.getImages().stream()
				.map(DiaryResponseConverter::toDiaryImageDto)
				.collect(Collectors.toList()))
			.build();
	}

	public static DiaryResponse.DiaryImageByScheduleDto toDiaryImageDto(Image image) {
		return DiaryResponse.DiaryImageByScheduleDto.builder()
			.id(image.getId())
			.url(image.getImgUrl())
			.build();
	}

	public static DiaryResponse.GetDiaryByUserDto toGetDiaryByUserRes(
		ScheduleProjection.DiaryByUserDto diaryByUserDto) {
		return DiaryResponse.GetDiaryByUserDto.builder()
			.scheduleId(diaryByUserDto.getScheduleId())
			.contents(diaryByUserDto.getContents())
			.images(diaryByUserDto.getImages().stream()
				.map(DiaryResponseConverter::toDiaryImageByUserDto)
				.collect(Collectors.toList()))
			.build();
	}

	public static DiaryResponse.DiaryImageByUserDto toDiaryImageByUserDto(Image image) {
		return DiaryResponse.DiaryImageByUserDto.builder()
			.id(image.getId())
			.url(image.getImgUrl())
			.build();
	}

}
