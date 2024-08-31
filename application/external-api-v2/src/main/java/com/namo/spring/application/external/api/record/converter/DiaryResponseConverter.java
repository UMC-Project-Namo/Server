package com.namo.spring.application.external.api.record.converter;

import com.namo.spring.application.external.api.record.dto.DiaryResponse;
import com.namo.spring.db.mysql.domains.record.entity.Diary;
import com.namo.spring.db.mysql.domains.record.entity.DiaryImage;

public class DiaryResponseConverter {

	public static DiaryResponse.DiaryDto toDiaryDto(Diary diary) {
		return DiaryResponse.DiaryDto.builder()
			.diaryId(diary.getId())
			.content(diary.getContent())
			.enjoyRating(diary.getEnjoyRating())
			.diaryImages(diary.getImages().stream()
				.map(DiaryResponseConverter::toDiaryImageDto)
				.toList())
			.build();
	}

	public static DiaryResponse.DiaryImageDto toDiaryImageDto(DiaryImage image) {
		return DiaryResponse.DiaryImageDto.builder()
			.orderNumber(image.getImageOrder())
			.diaryImageId(image.getId())
			.imageUrl(image.getImageUrl())
			.build();
	}
}
