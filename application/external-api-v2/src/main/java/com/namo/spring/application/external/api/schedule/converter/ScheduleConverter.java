package com.namo.spring.application.external.api.schedule.converter;

import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.type.Location;
import com.namo.spring.db.mysql.domains.schedule.type.Period;

public class ScheduleConverter {
    private ScheduleConverter() {
        throw new IllegalStateException("Util Class");
    }

    public static Schedule toSchedule(String title, Period period, ScheduleRequest.LocationDto location, int type, String imageUrl, Integer participantCount, String participantNames) {
        return Schedule.builder()
                .title(title)
                .period(period)
                .location(toLocation(location))
                .scheduleType(type)
                .imageUrl(imageUrl)
                .participantCount(participantCount)
                .participantNicknames(participantNames)
                .build();
    }

    public static Location toLocation(ScheduleRequest.LocationDto dto) {
        return Location.builder()
                .longitude(dto.getLongitude())
                .latitude(dto.getLatitude())
                .name(dto.getLocationName())
                .kakaoLocationId(dto.getKakaoLocationId())
                .build();
    }
}
