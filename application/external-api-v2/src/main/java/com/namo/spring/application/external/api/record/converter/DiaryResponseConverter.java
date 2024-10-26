package com.namo.spring.application.external.api.record.converter;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.namo.spring.application.external.api.record.dto.DiaryResponse;
import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.record.entity.Diary;
import com.namo.spring.db.mysql.domains.record.entity.DiaryImage;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.type.ScheduleType;

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
                .scheduleStartDate(participant.getSchedule().getPeriod().getStartDate())
                .scheduleEndDate(participant.getSchedule().getPeriod().getEndDate())
                .categoryInfo(toCategoryInfoDto(participant.getCategory()))
                .scheduleId(participant.getSchedule().getId())
                .title(participant.getScheduleTitle())
                .diarySummary(toDiarySummaryDto(participant.getDiary()))
                .scheduleType(participant.getSchedule().getScheduleType())
                .participantInfo(toParticipantInfo(participant.getSchedule()))
                .build();
    }

    private static DiaryResponse.CategoryInfoDto toCategoryInfoDto(Category category) {
        return DiaryResponse.CategoryInfoDto.builder()
                .name(category.getName())
                .colorId(category.getPalette().getId())
                .build();
    }

    private static DiaryResponse.ParticipantInfo toParticipantInfo(Schedule schedule) {
        return DiaryResponse.ParticipantInfo.builder()
                .participantsCount(schedule.getParticipantCount())
                .participantsNames(schedule.getParticipantNicknames())
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

    public static DiaryResponse.DiaryExistDateDto toDiaryExistDateDto(List<Participant> participants, YearMonth yearMonth) {
        List<Integer> diaryForPersonal = new ArrayList<>();
        List<Integer> diaryForMeeting = new ArrayList<>();
        participants.forEach(participant -> {
            int dayOfMonth = participant.getSchedule().getStartDayOfMonth();
            if (participant.getSchedule().getScheduleType() ==  ScheduleType.PERSONAL.getValue()) {
                diaryForPersonal.add(dayOfMonth);
            } else if (participant.getSchedule().getScheduleType() == ScheduleType.MEETING.getValue()) {
                diaryForMeeting.add(dayOfMonth);
            }
        });
        return DiaryResponse.DiaryExistDateDto.builder()
                .year(yearMonth.getYear())
                .month(yearMonth.getMonth().getValue())
                .DiaryDateForPersonal(diaryForPersonal.stream().distinct().sorted().collect(Collectors.toList()))
                .DiaryDateForMeeting(diaryForMeeting.stream().distinct().sorted().collect(Collectors.toList()))
                .build();
    }

    public static DiaryResponse.DayOfDiaryDto toDayOfDiaryDto(Participant participant) {
        return DiaryResponse.DayOfDiaryDto.builder()
                .scheduleId(participant.getSchedule().getId())
                .scheduleType(participant.getSchedule().getScheduleType())
                .categoryInfo(toCategoryInfoDto(participant.getCategory()))
                .scheduleStartDate(participant.getSchedule().getPeriod().getStartDate())
                .scheduleEndDate(participant.getSchedule().getPeriod().getEndDate())
                .scheduleTitle(participant.getScheduleTitle())
                .diaryId(participant.getDiary().getId())
                .participantInfo(toParticipantInfo(participant.getSchedule()))
                .build();
    }
}
