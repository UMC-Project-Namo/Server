package com.namo.spring.application.external.api.schedule.converter;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleRequest;
import com.namo.spring.application.external.api.schedule.dto.ScheduleResponse;
import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.type.Period;
import com.namo.spring.db.mysql.domains.schedule.type.ScheduleType;
import com.namo.spring.db.mysql.domains.user.entity.Member;

public class ScheduleConverter {
    private ScheduleConverter() {
        throw new IllegalStateException("Util Class");
    }

    public static Schedule toBirthdaySchedule(String title, Period period){
        return Schedule.builder()
                .title(title)
                .period(period)
                .location(null)
                .scheduleType(ScheduleType.BIRTHDAY.getValue())
                .imageUrl(null)
                .participantCount(null)
                .participantNicknames(null)
                .build();
    }

    public static Schedule toSchedule(String title, Period period, MeetingScheduleRequest.LocationDto location,
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

}
