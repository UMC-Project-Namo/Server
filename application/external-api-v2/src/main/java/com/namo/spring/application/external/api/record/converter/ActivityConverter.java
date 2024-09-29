package com.namo.spring.application.external.api.record.converter;

import com.namo.spring.application.external.api.record.dto.ActivityRequest;
import com.namo.spring.db.mysql.domains.record.entity.Activity;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.type.Location;

public class ActivityConverter {

    public static Activity toActivity(Schedule schedule, ActivityRequest.CreateActivityDto request) {
        return Activity.builder()
                .schedule(schedule)
                .title(request.getTitle())
                .categoryTag(request.getTag())
                .location(toLocation(request.getLocation()))
                .startDate(request.getActivityStartDate())
                .endDate(request.getActivityEndDate())
                .build();
    }

    public static Location toLocation(ActivityRequest.ActivityLocationDto request){
        return Location.builder()
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .kakaoLocationId(request.getKakaoLocationId())
                .name(request.getLocationName())
                .build();
    }
}
