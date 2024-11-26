package com.namo.spring.application.external.api.guest.converter;

import com.namo.spring.application.external.api.guest.dto.GuestMeetingResponse;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;

public class GuestMeetingResponseConverter {

    public static GuestMeetingResponse.GetMeetingScheduleInfoDto toGetMeetingScheduleInfoDto(
            Schedule schedule) {
        return GuestMeetingResponse.GetMeetingScheduleInfoDto.builder()
                .scheduleId(schedule.getId())
                .title(schedule.getTitle())
                .startDate(schedule.getPeriod().getStartDate())
                .imageUrl(schedule.getImageUrl())
                .build();
    }
}
