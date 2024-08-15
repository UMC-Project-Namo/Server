package com.namo.spring.application.external.api.schedule.converter;

import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.type.Location;
import com.namo.spring.db.mysql.domains.schedule.type.Period;
import com.namo.spring.db.mysql.domains.schedule.type.ScheduleType;

public class ScheduleConverter {
    private ScheduleConverter() {
        throw new IllegalStateException("Util Class");
    }

    public static Schedule toPersonalSchedule(String title, Period period, Location location) {
        return Schedule.builder()
                .title(title)
                .period(period)
                .location(location)
                .scheduleType(ScheduleType.PERSONAL.getValue())
                .imageUrl(null)
                .build();
    }

    public static Schedule toMeetingSchedule(String title, Period period, Location location, String imageUrl) {
        return Schedule.builder()
                .title(title)
                .period(period)
                .location(location)
                .scheduleType(ScheduleType.MEETING.getValue())
                .imageUrl(imageUrl)
                .build();
    }
}
