package com.namo.spring.application.external.api.schedule.converter;

import com.namo.spring.application.external.api.schedule.dto.GuestMeetingResponse;
import com.namo.spring.core.common.utils.DateUtil;
import com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GuestMeetingResponseConverter {

    public static List<GuestMeetingResponse.GetMonthlyMeetingParticipantScheduleDto> toGetMonthlyMeetingParticipantScheduleDtos(
            List<ScheduleParticipantQuery> participantsWithSchedule, Schedule curSchedule, Long curMemberId) {
        Map<Long, List<ScheduleParticipantQuery>> scheduleIdAndParticipant
                = participantsWithSchedule.stream()
                .collect(Collectors.groupingBy(participant -> participant.getSchedule().getId()));
        return scheduleIdAndParticipant.entrySet().stream()
                .map(entry -> toGetMonthlyMeetingParticipantScheduleDto(entry.getValue().get(0).getSchedule(),
                        entry.getValue(), curSchedule))
                .collect(Collectors.toList());
    }

    public static GuestMeetingResponse.GetMonthlyMeetingParticipantScheduleDto toGetMonthlyMeetingParticipantScheduleDto(
            Schedule schedule, List<ScheduleParticipantQuery> participant, Schedule curSchedule) {
        return GuestMeetingResponse.GetMonthlyMeetingParticipantScheduleDto.builder()
                .scheduleId(schedule.getId())
                .name(schedule.getTitle())
                .startDate(DateUtil.toSeconds(schedule.getPeriod().getStartDate()))
                .endDate(DateUtil.toSeconds(schedule.getPeriod().getEndDate()))
                .interval(schedule.getPeriod().getDayInterval())
                .participants(toUserParticipantDtos(participant))
                .isCurMeetingSchedule(schedule.getId().equals(curSchedule.getId()))
                .build();
    }

    private static List<GuestMeetingResponse.UserParticipantDto> toUserParticipantDtos(
            List<ScheduleParticipantQuery> participant) {
        return participant.stream()
                .map(GuestMeetingResponseConverter::toUserParticipantDto)
                .collect(Collectors.toList());
    }

    private static GuestMeetingResponse.UserParticipantDto toUserParticipantDto(ScheduleParticipantQuery
                                                                                        participant) {
        return GuestMeetingResponse.UserParticipantDto.builder()
                .participantId(participant.getParticipantId())
                .nickname(participant.getNickname())
                .colorId(participant.getParticipantPaletteId())
                .build();
    }
}
