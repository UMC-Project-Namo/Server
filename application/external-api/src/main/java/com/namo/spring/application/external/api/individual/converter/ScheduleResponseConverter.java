package com.namo.spring.application.external.api.individual.converter;

import com.namo.spring.application.external.api.individual.dto.ScheduleResponse;
import com.namo.spring.application.external.domain.group.domain.MoimMemoLocationImg;
import com.namo.spring.application.external.domain.group.domain.MoimScheduleAlarm;
import com.namo.spring.application.external.domain.group.domain.MoimScheduleAndUser;
import com.namo.spring.db.mysql.domains.individual.domain.Alarm;
import com.namo.spring.db.mysql.domains.individual.domain.Image;
import com.namo.spring.db.mysql.domains.individual.domain.Schedule;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleResponseConverter {
    public static ScheduleResponse.ScheduleIdDto toScheduleIdRes(Schedule schedule) {
        return ScheduleResponse.ScheduleIdDto.builder()
                .scheduleId(schedule.getId())
                .build();
    }

    public static ScheduleResponse.GetScheduleDto toGetScheduleRes(com.namo.spring.db.mysql.domains.individual.dto.ScheduleResponse.GetScheduleDto getScheduleDto) {
        Long startDate = getScheduleDto.getStartDate()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .getEpochSecond();
        Long endDate = getScheduleDto.getEndDate().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        List<Integer> alarmDates = getScheduleDto.getAlarms().stream().map(Alarm::getAlarmDate).toList();

        return ScheduleResponse.GetScheduleDto.builder()
                .scheduleId(getScheduleDto.getScheduleId())
                .name(getScheduleDto.getName())
                .startDate(startDate)
                .endDate(endDate)
                .alarmDate(alarmDates)
                .interval(getScheduleDto.getInterval())
                .x(getScheduleDto.getX())
                .y(getScheduleDto.getY())
                .locationName(getScheduleDto.getLocationName())
                .kakaoLocationId(getScheduleDto.getKakaoLocationId())
                .categoryId(getScheduleDto.getCategoryId())
                .hasDiary(getScheduleDto.getHasDiary())
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

    public static ScheduleResponse.DiaryDto toDiaryDto(com.namo.spring.db.mysql.domains.individual.dto.ScheduleResponse.DiaryDto diaryDto) {
        return ScheduleResponse.DiaryDto.builder()
                .scheduleId(diaryDto.getScheduleId())
                .name(diaryDto.getName())
                .startDate(diaryDto.getStartDate()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .getEpochSecond())
                .contents(diaryDto.getContents())
                .categoryId(diaryDto.getCategoryId())
                .color(diaryDto.getColor())
                .placeName(diaryDto.getPlaceName())
                .urls(diaryDto.getImages().stream()
                        .map(Image::getImgUrl)
                        .collect(Collectors.toList()))
                .build();
    }

    public static ScheduleResponse.DiaryDto toDiaryDto(MoimScheduleAndUser moimScheduleAndUser){
        return ScheduleResponse.DiaryDto.builder()
                .scheduleId(moimScheduleAndUser.getMoimSchedule().getId())
                .name(moimScheduleAndUser.getMoimSchedule().getName())
                .startDate(moimScheduleAndUser.getMoimSchedule()
                    .getPeriod()
                    .getStartDate()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .getEpochSecond())
                .contents(moimScheduleAndUser.getMemo())
                .categoryId(moimScheduleAndUser.getCategory().getId())
                .color(moimScheduleAndUser.getCategory().getPalette().getId())
                .placeName(moimScheduleAndUser.getMoimSchedule().getLocation().getLocationName())
                .urls(moimScheduleAndUser.getMoimSchedule().getMoimMemo()
                    .getMoimMemoLocations()
                    .stream()
                    .flatMap(location -> location.getMoimMemoLocationImgs().stream())
                    .map(MoimMemoLocationImg::getUrl)
                    .limit(3)
                    .collect(Collectors.toList()))
                .build();
    }
}
