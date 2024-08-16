package com.namo.spring.application.external.api.schedule.converter;

import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.type.Location;
import com.namo.spring.db.mysql.domains.schedule.type.Period;

public class ScheduleConverter {
    private ScheduleConverter() {
        throw new IllegalStateException("Util Class");
    }

    public static Schedule toSchedule(String title, Period period, Location location, int type, String imageUrl) {
        return Schedule.builder()
                .title(title)
                .period(period)
                .location(location)
                .scheduleType(type)
                .imageUrl(imageUrl)
                .build();
    }
}
