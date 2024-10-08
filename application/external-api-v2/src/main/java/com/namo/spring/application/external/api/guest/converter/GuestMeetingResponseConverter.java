package com.namo.spring.application.external.api.guest.converter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.namo.spring.application.external.api.guest.dto.GuestMeetingResponse;
import com.namo.spring.core.common.utils.DateUtil;
import com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery;
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
