package com.namo.spring.application.external.api.schedule.converter;

import com.namo.spring.application.external.api.schedule.dto.ScheduleResponse;
import com.namo.spring.application.external.api.schedule.dto.interfaces.LocationDtoInterface;
import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.type.Period;
import com.namo.spring.db.mysql.domains.schedule.type.ScheduleType;
import com.namo.spring.db.mysql.domains.user.entity.Anonymous;
import com.namo.spring.db.mysql.domains.user.entity.User;

public class ScheduleConverter {
    private ScheduleConverter() {
        throw new IllegalStateException("Util Class");
    }

    public static Schedule toBirthdaySchedule(String title, String nickname, Period period){
        return Schedule.builder()
                .title(title)
                .period(period)
                .location(null)
                .scheduleType(ScheduleType.BIRTHDAY.getValue())
                .imageUrl(null)
                .participantCount(1)
                .participantNicknames(nickname)
                .build();
    }

    public static Schedule toSchedule(String title, Period period, LocationDtoInterface location,
            int type,
            String imageUrl, Integer participantCount, String participantNames) {
        return Schedule.builder()
                .title(title)
                .period(period)
                .location(location != null ? LocationConverter.toLocation(location) : null)
                .scheduleType(type)
                .imageUrl(imageUrl)
                .participantCount(participantCount)
                .participantNicknames(participantNames)
                .build();
    }

    public static ScheduleResponse.ScheduleSummaryDto toScheduleSummaryDto(Participant participant) {
        Schedule schedule = participant.getSchedule();
        return ScheduleResponse.ScheduleSummaryDto.builder()
                .scheduleId(schedule.getId())
                .scheduleTitle(schedule.getTitle())
                .scheduleStartDate(schedule.getPeriod().getStartDate())
                .locationInfo(LocationConverter.toLocationInfoDto(schedule.getLocation()))
                .categoryInfo(toCategoryInfoDto(participant.getCategory()))
                .hasDiary(participant.isHasDiary())
                .participantCount(schedule.getParticipantCount())
                .participantInfo(schedule.getParticipantList().stream()
                        .map(ScheduleConverter::toParticipantInfoDto)
                        .toList())
                .build();
    }

    private static ScheduleResponse.CategoryInfoDto toCategoryInfoDto(Category category) {
        return ScheduleResponse.CategoryInfoDto.builder()
                .name(category.getName())
                .colorId(category.getPalette().getId())
                .build();
    }

    private static ScheduleResponse.ParticipantInfoDto toParticipantInfoDto(Participant participant){
        User user = participant.getUser();
        return ScheduleResponse.ParticipantInfoDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .isGuest(user instanceof Anonymous)
                .build();
    }
}
