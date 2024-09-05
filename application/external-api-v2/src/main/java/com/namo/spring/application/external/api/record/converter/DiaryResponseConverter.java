package com.namo.spring.application.external.api.record.converter;

import java.util.List;
import java.util.stream.Collectors;

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

	public static DiaryResponse.DiaryExistDateDto toDiaryExistDateDto(List<Participant> participants, int year,
		int month) {
		return DiaryResponse.DiaryExistDateDto.builder()
			.year(year)
			.month(month)
			.dates(participants.stream()
				.map(participant -> participant.getSchedule().getPeriod().getStartDate().getDayOfMonth()) // 날짜만 추출
				.distinct() // 중복 제거 (만약 중복된 날짜가 있을 경우)
				.sorted()
				.collect(Collectors.toList()))
			.build();
	}
}
