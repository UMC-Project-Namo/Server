package com.example.namo2.domain.individual.application.converter;

import com.example.namo2.domain.group.domain.MoimScheduleAlarm;
import com.example.namo2.domain.group.domain.MoimScheduleAndUser;
import com.example.namo2.domain.individual.domain.Alarm;
import com.example.namo2.domain.individual.domain.Schedule;
import com.example.namo2.domain.individual.ui.dto.ScheduleResponse;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleResponseConverter {
    public static ScheduleResponse.ScheduleIdDto toScheduleIdRes(Schedule schedule) {
        return ScheduleResponse.ScheduleIdDto.builder()
                .scheduleId(schedule.getId())
                .build();
    }

    public static ScheduleResponse.GetScheduleDto toGetScheduleRes(Schedule schedule) {
        Long startDate = schedule.getPeriod()
                .getStartDate()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        Long endDate = schedule.getPeriod().getEndDate().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        List<Integer> alarmDates = schedule.getAlarms().stream().map(Alarm::getAlarmDate).toList();

        return ScheduleResponse.GetScheduleDto.builder()
                .scheduleId(schedule.getId())
                .name(schedule.getName())
                .startDate(startDate)
                .endDate(endDate)
                .alarmDate(alarmDates)
                .interval(schedule.getPeriod().getDayInterval())
                .x(schedule.getLocation().getX())
                .y(schedule.getLocation().getY())
                .locationName(schedule.getLocation().getLocationName())
                .kakaoLocationId(schedule.getLocation().getKakaoLocationId())
                .categoryId(schedule.getCategory().getId())
                .hasDiary(schedule.getHasDiary())
                .isMoimSchedule(false)
                .build();
    }

    public static ScheduleResponse.GetScheduleDto toGetScheduleRes(MoimScheduleAndUser moimScheduleAndUser) {
        Long startDate = moimScheduleAndUser.getMoimSchedule().getPeriod().getStartDate()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        Long endDate = moimScheduleAndUser.getMoimSchedule().getPeriod().getEndDate()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        List<Integer> alarmDates = moimScheduleAndUser.getMoimScheduleAlarms().stream()
                .map(MoimScheduleAlarm::getAlarmDate).toList();

        return ScheduleResponse.GetScheduleDto.builder()
                .scheduleId(moimScheduleAndUser.getMoimSchedule().getId())
                .name(moimScheduleAndUser.getMoimSchedule().getName())
                .startDate(startDate)
                .endDate(endDate)
                .alarmDate(alarmDates)
                .interval(moimScheduleAndUser.getMoimSchedule().getPeriod().getDayInterval())
                .x(moimScheduleAndUser.getMoimSchedule().getLocation().getX())
                .y(moimScheduleAndUser.getMoimSchedule().getLocation().getY())
                .locationName(moimScheduleAndUser.getMoimSchedule().getLocation().getLocationName())
                .kakaoLocationId(moimScheduleAndUser.getMoimSchedule().getLocation().getKakaoLocationId())
                .categoryId(moimScheduleAndUser.getCategory().getId())
                .hasDiary(decideHasDiary(moimScheduleAndUser))
                .isMoimSchedule(true)
                .build();
    }

    private static Boolean decideHasDiary(MoimScheduleAndUser moimScheduleAndUser) {
        if (moimScheduleAndUser.getMoimSchedule().getMoimMemo() != null && moimScheduleAndUser.getMemo() != null) {
            return Boolean.TRUE;
        }
        if (moimScheduleAndUser.getMoimSchedule().getMoimMemo() != null && moimScheduleAndUser.getMemo() == null) {
            return Boolean.FALSE;
        }
        return null;
    }

    public static ScheduleResponse.DiaryDto toDiaryDto(Schedule schedule) {
        return ScheduleResponse.DiaryDto.builder()
                .scheduleId(schedule.getId())
                .name(schedule.getName())
                .startDate(schedule.getPeriod().getStartDate()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .getEpochSecond())
                .contents(schedule.getContents())
                .categoryId(schedule.getCategory().getId())
                .color(schedule.getCategory().getPalette().getId())
                .placeName(schedule.getLocation().getLocationName())
                .urls(schedule.getImages().stream()
                        .map(image -> image.getImgUrl())
                        .collect(Collectors.toList()))
                .build();
    }
}
