package com.namo.spring.application.external.api.schedule.service;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleRequest;
import com.namo.spring.application.external.api.schedule.dto.PersonalScheduleRequest;
import com.namo.spring.core.infra.common.properties.ImageUrlProperties;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.service.ScheduleService;
import com.namo.spring.db.mysql.domains.schedule.type.Period;
import com.namo.spring.db.mysql.domains.schedule.type.ScheduleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.namo.spring.application.external.api.schedule.converter.ScheduleConverter.toSchedule;

@Component
@RequiredArgsConstructor
public class ScheduleMaker {
    private static final int PERSONAL_SCHEDULE_TYPE = ScheduleType.PERSONAL.getValue();
    private static final int MEETING_SCHEDULE_TYPE = ScheduleType.MEETING.getValue();

    private final ScheduleService scheduleService;
    private final ImageUrlProperties imageUrlProperties;

    public Schedule createPersonalSchedule(PersonalScheduleRequest.PostPersonalScheduleDto dto, Period period,
            String nickname) {
        Schedule schedule = toSchedule(dto.getTitle(), period, dto.getLocation(), PERSONAL_SCHEDULE_TYPE, null, 1,
                nickname);
        return scheduleService.createSchedule(schedule);
    }

    public Schedule createMeetingSchedule(MeetingScheduleRequest.PostMeetingScheduleDto dto, Period period) {
        String imageUrl = dto.getImageUrl() != null ? dto.getImageUrl() : imageUrlProperties.getMeeting();
        Schedule schedule = toSchedule(dto.getTitle(), period, dto.getLocation(), MEETING_SCHEDULE_TYPE, imageUrl, 0, "");
        return scheduleService.createSchedule(schedule);
    }

}
