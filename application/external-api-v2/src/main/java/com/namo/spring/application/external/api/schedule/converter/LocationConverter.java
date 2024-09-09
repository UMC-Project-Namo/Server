package com.namo.spring.application.external.api.schedule.converter;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleRequest;
import com.namo.spring.application.external.api.schedule.dto.ScheduleResponse;
import com.namo.spring.db.mysql.domains.schedule.type.Location;

public class LocationConverter {
    private LocationConverter() {
        throw new IllegalStateException("Util Class");
    }

    public static Location toLocation(MeetingScheduleRequest.LocationDto dto) {
        return Location.builder()
                .longitude(dto.getLongitude())
                .latitude(dto.getLatitude())
                .name(dto.getLocationName())
                .kakaoLocationId(dto.getKakaoLocationId())
                .build();
    }

    public static ScheduleResponse.LocationInfoDto toLocationInfoDto(Location location) {
        return ScheduleResponse.LocationInfoDto.builder()
                .kakaoLocationId(location.getKakaoLocationId())
                .locationName(location.getName())
                .build();
    }
}
