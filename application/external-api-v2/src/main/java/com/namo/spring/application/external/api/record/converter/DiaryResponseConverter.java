package com.namo.spring.application.external.api.record.converter;

import com.namo.spring.application.external.api.record.dto.DiaryResponse;
import com.namo.spring.db.mysql.domains.record.entity.Diary;
import com.namo.spring.db.mysql.domains.record.entity.DiaryImage;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;

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

	public static DiaryResponse.DiaryArchiveDto toDiaryArchiveDto(Participant participant) {
		return DiaryResponse.DiaryArchiveDto.builder()
			.scheduleDate(participant.getSchedule().getPeriod().getStartDate())
			.scheduleId(participant.getSchedule().getId())
			.title(participant.getSchedule().getTitle())
			.diarySummary(toDiarySummaryDto(participant.getDiary()))
			.scheduleType(participant.getSchedule().getScheduleType())
			.participantsCount(participant.getSchedule().getParticipantCount())
			.participantsNames(participant.getSchedule().getParticipantNicknames())
			.build();
	}

	private static DiaryResponse.DiarySummaryDto toDiarySummaryDto(Diary diary) {
		return DiaryResponse.DiarySummaryDto.builder()
			.diaryId(diary.getId())
			.content(diary.getContent())
			.diaryImages(diary.getImages().stream()
				.map(DiaryResponseConverter::toDiaryImageDto)
				.toList())
			.build();
	}
}
